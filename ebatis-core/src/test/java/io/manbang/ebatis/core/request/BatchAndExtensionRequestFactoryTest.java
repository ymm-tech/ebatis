package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.Mapper;
import io.manbang.ebatis.core.annotation.NativeOperation;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.Reindex;
import io.manbang.ebatis.core.extension.OperationExtension;
import io.manbang.ebatis.core.extension.OperationExtensionRegistry;
import io.manbang.ebatis.core.meta.MapperInterface;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BatchAndExtensionRequestFactoryTest {
    @After
    public void cleanExtension() {
        OperationExtensionRegistry.unregister("echo");
    }

    @Test
    public void shouldBuildManagedBulkOptions() throws Exception {
        MapperInterface mapper = MapperInterface.of(BatchMapper.class);
        MethodMeta meta = mapper.getMapperMethod(BatchMapper.class.getMethod("bulk", java.util.List.class));
        ManagedBulkRequest request = (ManagedBulkRequest) RequestFactory.bulk().create(meta,
                Arrays.asList(new Doc("1"), new Doc("2"), new Doc("3")));

        assertEquals(3, request.numberOfActions());
        assertEquals(2, request.getBatchSize());
        assertEquals(2, request.getConcurrentRequests());
        assertEquals(4, request.getMaxRetries());
        assertEquals(25L, request.getRetryBackoff().millis());
    }

    @Test
    public void shouldBuildCompletedAndSubmittedReindexRequests() throws Exception {
        MapperInterface mapper = MapperInterface.of(ReindexMapper.class);
        MethodMeta completedMeta = mapper.getMapperMethod(ReindexMapper.class.getMethod("copy"));
        ReindexActionRequest completed = RequestFactory.reindex().create(completedMeta);
        assertTrue(completed.isWaitForCompletion());
        assertTrue(completed.getRequest().getSearchRequest().source().query() instanceof MatchAllQueryBuilder);
        assertEquals("ebatis-destination", completed.getRequest().getDestination().index());
        assertEquals(2, completed.getRequest().getSlices());

        MethodMeta submittedMeta = mapper.getMapperMethod(ReindexMapper.class.getMethod("submit"));
        ReindexActionRequest submitted = RequestFactory.reindex().create(submittedMeta);
        assertTrue(!submitted.isWaitForCompletion());
    }

    @Test
    public void shouldRegisterCompleteNativeOperationChain() throws Exception {
        OperationExtensionRegistry.register(new EchoExtension());
        MapperInterface mapper = MapperInterface.of(NativeMapper.class);
        assertEquals("echo", mapper.getMapperMethod(NativeMapper.class.getMethod("echo", String.class))
                .getAnnotation(NativeOperation.class).value());
    }

    @Mapper(indices = "ebatis-source")
    private interface BatchMapper {
        @Bulk(bulkType = BulkType.INDEX, batchSize = 2, concurrentRequests = 2,
                maxRetries = 4, retryBackoff = "25ms", index = @Index)
        BulkResponse bulk(java.util.List<Doc> documents);
    }

    @Mapper(indices = "ebatis-source")
    private interface ReindexMapper {
        @Reindex(destination = "ebatis-destination", queryType = QueryType.MATCH_ALL, slices = 2)
        BulkByScrollResponse copy();

        @Reindex(destination = "ebatis-destination", queryType = QueryType.MATCH_ALL, waitForCompletion = false)
        String submit();
    }

    @Mapper(indices = "unused")
    private interface NativeMapper {
        @NativeOperation("echo")
        String echo(String value);
    }

    private static class Doc implements IdProvider {
        private final String id;

        private Doc(String id) { this.id = id; }

        public String getId() { return id; }

        @Override
        public String id() { return id; }
    }

    private static class EchoRequest extends ActionRequest {
        @Override
        public ActionRequestValidationException validate() { return null; }
    }

    private static class EchoResponse extends ActionResponse {
        @Override
        public void writeTo(StreamOutput out) throws IOException { }
    }

    private static class EchoExtension implements OperationExtension<EchoRequest, EchoResponse> {
        @Override public String name() { return "echo"; }
        @Override public RequestFactory<EchoRequest> requestFactory() { return (method, args) -> new EchoRequest(); }
        @Override public void execute(io.manbang.ebatis.core.cluster.Cluster cluster, EchoRequest request,
                                      ActionListener<EchoResponse> listener) { listener.onResponse(new EchoResponse()); }
        @Override public ResponseExtractor<?> responseExtractor(MethodMeta method) { return response -> "echo"; }
    }
}

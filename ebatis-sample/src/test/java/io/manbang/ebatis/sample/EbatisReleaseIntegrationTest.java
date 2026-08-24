package io.manbang.ebatis.sample;

import io.manbang.ebatis.core.annotation.Agg;
import io.manbang.ebatis.core.annotation.Bulk;
import io.manbang.ebatis.core.annotation.BulkType;
import io.manbang.ebatis.core.annotation.ClosePointInTime;
import io.manbang.ebatis.core.annotation.Count;
import io.manbang.ebatis.core.annotation.DeleteByQuery;
import io.manbang.ebatis.core.annotation.DocumentExists;
import io.manbang.ebatis.core.annotation.GetSource;
import io.manbang.ebatis.core.annotation.Index;
import io.manbang.ebatis.core.annotation.OpenPointInTime;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.annotation.RefreshPolicy;
import io.manbang.ebatis.core.annotation.Reindex;
import io.manbang.ebatis.core.annotation.Search;
import io.manbang.ebatis.core.annotation.TaskResult;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.domain.Sort;
import io.manbang.ebatis.core.provider.AggregationBuilderProvider;
import io.manbang.ebatis.core.provider.IdProvider;
import io.manbang.ebatis.core.provider.PointTimeProvider;
import io.manbang.ebatis.core.provider.QueryBuilderProvider;
import io.manbang.ebatis.core.provider.SearchAfterProvider;
import io.manbang.ebatis.core.provider.SortProvider;
import io.manbang.ebatis.core.proxy.MapperProxyFactory;
import io.manbang.ebatis.spring.annotation.EasyMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.tasks.GetTaskResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EbatisReleaseIntegrationTest {
    private static final String SOURCE = "ebatis_release_it_source";
    private static final String DESTINATION = "ebatis_release_it_destination";
    private static final String ASYNC_DESTINATION = "ebatis_release_it_async_destination";

    private Cluster cluster;
    private String sourceIndex;
    private String destinationIndex;
    private String asyncDestinationIndex;
    private ReleaseMapper mapper;

    @Before
    public void setUp() throws IOException {
        String host = System.getProperty("ebatis.it.host", "");
        String username = System.getProperty("ebatis.it.username", "");
        String password = System.getProperty("ebatis.it.password", "");
        Assume.assumeTrue("通过系统属性显式启用本地 ES 集成测试",
                !host.isEmpty() && !username.isEmpty() && !password.isEmpty());
        cluster = Cluster.simple(host, Integer.getInteger("ebatis.it.port", 9200), username, password);
        ensureAliasUnused(SOURCE);
        ensureAliasUnused(DESTINATION);
        ensureAliasUnused(ASYNC_DESTINATION);
        String suffix = UUID.randomUUID().toString().replace("-", "");
        sourceIndex = createIndex(SOURCE, suffix);
        destinationIndex = createIndex(DESTINATION, suffix);
        asyncDestinationIndex = createIndex(ASYNC_DESTINATION, suffix);
        mapper = MapperProxyFactory.getMapperProxy(ReleaseMapper.class, "chip");
    }

    @After
    public void tearDown() throws IOException {
        if (cluster == null) {
            return;
        }
        try {
            deleteOwnedIndex(sourceIndex);
            deleteOwnedIndex(destinationIndex);
            deleteOwnedIndex(asyncDestinationIndex);
        } finally {
            cluster.close();
        }
    }

    @Test
    public void shouldCoverCrudCountPitAggregationByQueryBulkReindexAndTask() {
        BulkResponse bulk = mapper.bulk(Arrays.asList(
                new Doc("1", "alpha", 10), new Doc("2", "beta", 20), new Doc("3", "gamma", 30)));
        assertFalse(bulk.buildFailureMessage(), bulk.hasFailures());
        assertEquals(3L, mapper.count());
        assertTrue(mapper.exists("1"));
        assertEquals("alpha", mapper.source("1").get("name"));

        String pitId = mapper.openPit();
        SearchResponse firstPage = mapper.pit(new PitCondition(pitId, null));
        assertNotNull(firstPage.pointInTimeId());
        assertTrue(firstPage.getHits().getHits().length > 0);
        Object[] sortValues = firstPage.getHits().getAt(0).getSortValues();
        SearchResponse secondPage = mapper.pit(new PitCondition(firstPage.pointInTimeId(), sortValues));
        assertNotNull(secondPage.pointInTimeId());
        assertTrue(mapper.closePit(secondPage.pointInTimeId()));

        SearchResponse aggregation = mapper.aggregate(new AggCondition());
        assertNotNull(aggregation.getAggregations().get("by_name"));

        BulkByScrollResponse reindex = mapper.reindex();
        assertEquals(3L, reindex.getCreated());

        String taskId = mapper.submitReindex();
        assertNotNull(taskId);
        Optional<GetTaskResponse> task = mapper.task(taskId);
        assertTrue(task.isPresent());
        assertTrue(task.get().isCompleted());

        assertEquals(3L, mapper.deleteAll(new MatchAllCondition()));
        assertEquals(0L, mapper.count());
    }

    private void ensureAliasUnused(String alias) throws IOException {
        assertFalse("测试别名已被占用，拒绝覆盖: " + alias,
                cluster.highLevelClient().indices().exists(new GetIndexRequest(alias), RequestOptions.DEFAULT));
    }

    private String createIndex(String alias, String suffix) throws IOException {
        String index = alias + "_" + suffix;
        String mapping = "{\"mappings\":{\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"},\"name\":{\"type\":\"keyword\"},\"amount\":{\"type\":\"integer\"}}}}";
        CreateIndexRequest request = new CreateIndexRequest(index).source(mapping, XContentType.JSON)
                .alias(new org.elasticsearch.action.admin.indices.alias.Alias(alias).writeIndex(true));
        assertTrue(cluster.highLevelClient().indices().create(request, RequestOptions.DEFAULT).isAcknowledged());
        return index;
    }

    private void deleteOwnedIndex(String index) throws IOException {
        if (index != null && index.startsWith("ebatis_release_it_")
                && cluster.highLevelClient().indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT)) {
            cluster.highLevelClient().indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        }
    }

    @EasyMapper(indices = SOURCE, clusterRouter = "chip")
    public interface ReleaseMapper {
        @Bulk(bulkType = BulkType.INDEX, batchSize = 2, concurrentRequests = 2,
                refreshPolicy = "true", index = @Index(refreshPolicy = RefreshPolicy.IMMEDIATE))
        BulkResponse bulk(java.util.List<Doc> documents);

        @Count(queryType = QueryType.MATCH_ALL)
        long count();

        @DocumentExists boolean exists(String id);
        @GetSource Map<String, Object> source(String id);
        @OpenPointInTime(keepAlive = "1m") String openPit();
        @ClosePointInTime boolean closePit(String pitId);
        @Search(trackTotalHits = true) SearchResponse pit(PitCondition condition);
        @Agg(aggOnly = true) SearchResponse aggregate(AggCondition condition);

        @Reindex(destination = DESTINATION, queryType = QueryType.MATCH_ALL, refresh = true, requireAlias = true)
        BulkByScrollResponse reindex();

        @Reindex(destination = ASYNC_DESTINATION, queryType = QueryType.MATCH_ALL,
                waitForCompletion = false, requireAlias = true)
        String submitReindex();

        @TaskResult(waitForCompletion = true, timeout = "30s")
        Optional<GetTaskResponse> task(String taskId);

        @DeleteByQuery(refresh = true)
        long deleteAll(MatchAllCondition condition);
    }

    public static class Doc implements IdProvider {
        private String id;
        private String name;
        private int amount;
        public Doc() { }
        public Doc(String id, String name, int amount) { this.id = id; this.name = name; this.amount = amount; }
        public String getId() { return id; }
        public String getName() { return name; }
        public int getAmount() { return amount; }
        @Override public String id() { return id; }
    }

    public static class MatchAllCondition implements QueryBuilderProvider {
        @Override public QueryBuilder queryBuilder() { return QueryBuilders.matchAllQuery(); }
    }

    public static class PitCondition extends MatchAllCondition
            implements PointTimeProvider, SearchAfterProvider, SortProvider {
        private final String pitId;
        private final Object[] sortValues;
        public PitCondition(String pitId, Object[] sortValues) { this.pitId = pitId; this.sortValues = sortValues; }
        @Override public String pitId() { return pitId; }
        @Override public String keepAlive() { return "1m"; }
        @Override public Object[] sortValues() { return sortValues; }
        @Override public Sort[] getSorts() { return new Sort[]{Sort.fieldAsc("_shard_doc")}; }
    }

    public static class AggCondition extends MatchAllCondition implements AggregationBuilderProvider {
        @Override public AggregationBuilder aggregationBuilder() {
            return AggregationBuilders.terms("by_name").field("name");
        }
    }
}

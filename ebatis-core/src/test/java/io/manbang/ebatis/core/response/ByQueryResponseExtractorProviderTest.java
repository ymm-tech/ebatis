package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.generic.GenericType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ByQueryResponseExtractorProviderTest {
    private final BulkByScrollResponse response = response();

    @Test
    public void shouldExtractDeleteByQueryResponseTypes() {
        DeleteByQueryResponseExtractorProvider provider = new DeleteByQueryResponseExtractorProvider();

        assertSame(response, extract(provider, BulkByScrollResponse.class));
        assertSame(response.getStatus(), extract(provider, BulkByScrollTask.Status.class));
        assertEquals(3L, extract(provider, Long.class));
        assertTrue((Boolean) extract(provider, Boolean.class));

        ByQueryResponse result = (ByQueryResponse) extract(provider, ByQueryResponse.class);
        assertEquals(10L, result.getTotal());
        assertEquals(3L, result.getDeleted());
        assertEquals(2L, result.getBulkRetries());
    }

    @Test
    public void shouldExtractUpdateByQueryResponseTypes() {
        UpdateByQueryResponseExtractorProvider provider = new UpdateByQueryResponseExtractorProvider();

        assertSame(response, extract(provider, BulkByScrollResponse.class));
        assertSame(response.getStatus(), extract(provider, BulkByScrollTask.Status.class));
        assertEquals(4L, extract(provider, Long.class));
        assertTrue((Boolean) extract(provider, Boolean.class));
    }

    private Object extract(AbstractResponseExtractorProvider provider, Class<?> resultType) {
        ResponseExtractor<?> extractor = provider.getResponseExtractor(null, GenericType.forType(resultType));
        return extractor.extractData(response);
    }

    private static BulkByScrollResponse response() {
        BulkByScrollTask.Status status = new BulkByScrollTask.Status(null, 10, 4, 1, 3, 2,
                0, 2, 2, 1, TimeValue.timeValueMillis(25), 10.0f, null,
                TimeValue.timeValueMillis(5));
        return new BulkByScrollResponse(TimeValue.timeValueMillis(100), status,
                Collections.emptyList(), Collections.emptyList(), false);
    }
}

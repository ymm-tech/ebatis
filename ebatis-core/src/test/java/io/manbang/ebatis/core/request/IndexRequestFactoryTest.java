package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Index;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexRequestFactoryTest {
    @Test
    public void shouldApplyPipelineAndFinalPipelineSeparately() throws NoSuchMethodException {
        Index annotation = Mapper.class.getMethod("index", Object.class).getAnnotation(Index.class);
        IndexRequest request = new IndexRequest("orders");

        IndexRequestFactory.INSTANCE.setAnnotationMeta(request, annotation);

        assertEquals("normalize", request.getPipeline());
        assertEquals("finalize", request.getFinalPipeline());
    }

    private interface Mapper {
        @Index(pipeline = "normalize", finalPipeline = "finalize", requireAlias = true)
        void index(Object document);
    }
}

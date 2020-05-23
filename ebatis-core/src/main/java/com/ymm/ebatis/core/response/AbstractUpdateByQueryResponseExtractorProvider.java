package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:04
 */
public abstract class AbstractUpdateByQueryResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractUpdateByQueryResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.UPDATE_BY_QUERY, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();
        if (BulkByScrollResponse.class == resultClass) {
            return RawUpdateByQueryResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

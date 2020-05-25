package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 14:06
 */
public abstract class AbstractDeleteByQueryResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractDeleteByQueryResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.DELETE_BY_QUERY, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (BulkByScrollResponse.class == resultClass) {
            return RawDeleteByQueryResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

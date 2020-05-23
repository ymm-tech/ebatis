package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.executor.BooleanBulkResponseExtractor;
import com.ymm.ebatis.core.executor.RawBulkResponseExtractor;
import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:14
 */
public abstract class AbstractBulkResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractBulkResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.BULK, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (Boolean.class == resultClass || boolean.class == resultClass) {
            return BooleanBulkResponseExtractor.INSTANCE;
        } else if (BulkResponse.class == resultClass) {
            return RawBulkResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

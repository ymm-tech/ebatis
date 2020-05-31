package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:32
 */
public abstract class AbstractUpdateResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractUpdateResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.UPDATE, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (UpdateResponse.class == resultClass) {
            return RawUpdateResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

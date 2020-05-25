package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;
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

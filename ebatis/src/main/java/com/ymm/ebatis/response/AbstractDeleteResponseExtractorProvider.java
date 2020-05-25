package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;
import org.elasticsearch.action.delete.DeleteResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 13:58
 */
public abstract class AbstractDeleteResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractDeleteResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.DELETE, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (DeleteResponse.class == resultClass) {
            return RawDeleteResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

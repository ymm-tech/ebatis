package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.executor.BooleanIndexResponseExtractor;
import com.ymm.ebatis.core.meta.RequestType;
import com.ymm.ebatis.core.meta.ResultType;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:02
 */
public abstract class AbstractIndexResponseExtractorProvider extends AbstractResponseExtractorProvider {
    protected AbstractIndexResponseExtractorProvider(ResultType... resultTypes) {
        super(RequestType.INDEX, resultTypes);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (Boolean.class == resultClass || boolean.class == resultClass) {
            return BooleanIndexResponseExtractor.INSTANCE;
        } else if (IndexResponse.class == resultClass) {
            return RawIndexResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

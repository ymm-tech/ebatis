package com.ymm.ebatis.response;

import com.ymm.ebatis.executor.BooleanIndexResponseExtractor;
import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;
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

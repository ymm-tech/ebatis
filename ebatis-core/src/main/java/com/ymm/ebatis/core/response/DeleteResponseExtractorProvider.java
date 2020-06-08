package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.delete.DeleteResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 13:58
 */
@AutoService(ResponseExtractorProvider.class)
public class DeleteResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public DeleteResponseExtractorProvider() {
        super(RequestType.DELETE);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (DeleteResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

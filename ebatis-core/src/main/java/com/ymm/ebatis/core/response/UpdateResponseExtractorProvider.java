package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:32
 */
@AutoService(ResponseExtractorProvider.class)
public class UpdateResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public UpdateResponseExtractorProvider() {
        super(RequestType.UPDATE);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (UpdateResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;

@AutoService(ResponseExtractorProvider.class)
public class DocumentExistsResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public DocumentExistsResponseExtractorProvider() {
        super(RequestType.DOCUMENT_EXISTS);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (DocumentExistsResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (boolean.class == resultClass || Boolean.class == resultClass) {
            return response -> ((DocumentExistsResponse) response).isExists();
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported DocumentExists return type: " + genericType);
    }
}

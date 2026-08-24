package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.annotation.NativeOperation;
import io.manbang.ebatis.core.extension.OperationExtensionRegistry;
import io.manbang.ebatis.core.meta.MethodMeta;

@AutoService(ResponseExtractorProvider.class)
public class NativeOperationResponseExtractorProvider implements ResponseExtractorProvider {
    @Override
    public boolean support(MethodMeta method) {
        return method.getRequestType() == io.manbang.ebatis.core.meta.RequestType.NATIVE_OPERATION;
    }

    @Override
    public ResponseExtractor<?> getResponseExtractor(MethodMeta method) {
        String name = method.getAnnotation(NativeOperation.class).value();
        return OperationExtensionRegistry.required(name).responseExtractor(method);
    }
}

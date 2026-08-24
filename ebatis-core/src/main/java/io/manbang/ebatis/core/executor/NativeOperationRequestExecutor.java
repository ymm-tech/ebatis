package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.annotation.NativeOperation;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.extension.OperationExtension;
import io.manbang.ebatis.core.extension.OperationExtensionRegistry;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;

import java.util.concurrent.CompletableFuture;

class NativeOperationRequestExecutor implements RequestExecutor {
    static final NativeOperationRequestExecutor INSTANCE = new NativeOperationRequestExecutor();

    private NativeOperationRequestExecutor() {
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object execute(Cluster cluster, MethodMeta method, Object[] args) {
        String name = method.getAnnotation(NativeOperation.class).value();
        OperationExtension extension = OperationExtensionRegistry.required(name);
        ActionRequest request = (ActionRequest) extension.requestFactory().create(method, args);
        ResponseExtractor extractor = method.getResponseExtractor(args);
        CompletableFuture<Object> future = new CompletableFuture<>();
        extension.execute(cluster, request, ActionListener.wrap(response -> {
            try {
                future.complete(extractor.extractData((org.elasticsearch.action.ActionResponse) response));
            } catch (RuntimeException exception) {
                future.completeExceptionally(exception);
            }
        }, future::completeExceptionally));
        return method.getResultType().adaptResult(future);
    }
}

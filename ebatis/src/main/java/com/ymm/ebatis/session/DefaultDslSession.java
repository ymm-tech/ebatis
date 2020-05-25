package com.ymm.ebatis.session;

import com.ymm.ebatis.config.Env;
import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.domain.Context;
import com.ymm.ebatis.domain.ContextHolder;
import com.ymm.ebatis.exception.InvalidResponseException;
import com.ymm.ebatis.response.ResponseExtractor;
import org.apache.logging.log4j.util.BiConsumer;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2020/5/23 17:14
 */
public abstract class DefaultDslSession implements DslSession {
    private final Cluster cluster;


    public DefaultDslSession(Cluster cluster) {
        this.cluster = cluster;
        GetRequest request = new GetRequest("cargo", "1");
        request.routing();
    }


    @Override
    public <T> CompletableFuture<T> searchAsync(SearchRequest request, ResponseExtractor<T> extractor) {
        return performRequestAsync(cluster::searchAsync, request, extractor);
    }


    private <R extends ActionResponse, E> ActionListener<R> wrap(CompletableFuture<E> future, ResponseExtractor<E> extractor) {
        Context context = ContextHolder.getContext();

        return ActionListener.wrap(response -> {
            ContextHolder.setContext(context);

            try {
                boolean validated = extractor.validate(response);
                if (validated) {
                    future.complete(extractor.extractData(response));
                } else {
                    if (extractor.fallbackEnabled()) {
                        future.complete(extractor.fallback(null));
                    } else {
                        future.completeExceptionally(new InvalidResponseException(response.toString()));
                    }
                }
            } finally {
                ContextHolder.remove();
            }
        }, ex -> {
            future.completeExceptionally(ex);
            ContextHolder.remove();
        });
    }

    private <T extends ActionRequest, R extends ActionResponse, E> CompletableFuture<E> performRequestAsync(RequestExecutor<T, R> executor, T request, ResponseExtractor<E> extractor) {
        CompletableFuture<E> future = new CompletableFuture<>();
        if (Env.offlineEnabled()) {
            future.complete(extractor.empty());
            return future;
        }

        executor.execute(request, wrap(future, extractor));
        return future;
    }


    @FunctionalInterface
    private interface RequestExecutor<A extends ActionRequest, R> extends BiConsumer<A, ActionListener<R>> {

        /**
         * 执行ES请求
         *
         * @param request  ES请求
         * @param listener 响应监听器
         */
        default void execute(A request, ActionListener<R> listener) {
            accept(request, listener);
        }
    }
}

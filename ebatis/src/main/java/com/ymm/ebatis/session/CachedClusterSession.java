package com.ymm.ebatis.session;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.config.Env;
import com.ymm.ebatis.domain.Context;
import com.ymm.ebatis.domain.ContextHolder;
import com.ymm.ebatis.domain.Page;
import com.ymm.ebatis.domain.Pageable;
import com.ymm.ebatis.exception.InvalidResponseException;
import com.ymm.ebatis.response.ResponseExtractor;
import org.apache.logging.log4j.util.BiConsumer;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 集群亲和会话
 *
 * @author 章多亮
 * @since 2020/5/23 17:14
 */
class CachedClusterSession implements ClusterSession {
    private static final Map<Cluster, ClusterSession> CLUSTER_SESSIONS = new HashMap<>();
    private final Cluster cluster;

    private CachedClusterSession(Cluster cluster) {
        this.cluster = cluster;
    }

    static synchronized ClusterSession createOrGet(Cluster cluster) {
        return CLUSTER_SESSIONS.computeIfAbsent(cluster, CachedClusterSession::new);
    }

    @Override
    public <T> CompletableFuture<T> getAsync(GetRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> deleteAsync(DeleteRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> deleteByQueryAsync(DeleteByQueryRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateAsync(UpdateRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> updateByQueryAsync(UpdateByQueryRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> indexAsync(IndexRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> multiSearchAsync(MultiSearchRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> multiSearchAsync(MultiSearchRequest request, ResponseExtractor<T> extractor, Pageable[] pageable) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> searchAsync(SearchRequest request, ResponseExtractor<T> extractor) {
        return performRequestAsync(cluster::searchAsync, request, extractor);
    }

    @Override
    public <T> CompletableFuture<Page<T>> searchAsync(SearchRequest request, ResponseExtractor<T> extractor, Pageable pageable) {
        return null;
    }

    @Override
    public <T> CompletableFuture<T> bulkAsync(BulkRequest request, ResponseExtractor<T> extractor) {
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> mapperInterface) {
        return null;
    }

    @Override
    public Cluster getCluster() {
        return cluster;
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

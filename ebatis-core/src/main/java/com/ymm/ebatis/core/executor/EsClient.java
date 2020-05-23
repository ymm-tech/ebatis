package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.config.Env;
import com.ymm.ebatis.core.domain.Context;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.exception.InvalidResponseException;
import com.ymm.ebatis.core.interceptor.DefaultPostResponseInfo;
import com.ymm.ebatis.core.interceptor.DefaultPreResponseInfo;
import com.ymm.ebatis.core.interceptor.Interceptor;
import com.ymm.ebatis.core.interceptor.InterceptorFactory;
import com.ymm.ebatis.core.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.BiConsumer;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2019/12/26 19:12
 */
@Slf4j
public final class EsClient {
    private static final Interceptor INTERCEPTOR = InterceptorFactory.interceptors();

    private EsClient() {
        throw new UnsupportedOperationException();
    }

    /**
     * 同步索引
     *
     * @param cluster   客户端
     * @param request   索引请求
     * @param extractor 结果提取器
     * @return 结果
     */
    public static Object indexSync(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
        return indexAsync(cluster, request, extractor).join();
    }

    /**
     * 异步索引
     *
     * @param cluster   客户端
     * @param request   索引请求
     * @param extractor 结果提取器
     * @return 结果
     */
    public static CompletableFuture<Object> indexAsync(Cluster cluster, IndexRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::indexAsync, request, IndexResponse.class, extractor);
    }

    /**
     * 同步多搜索
     *
     * @param cluster   集群
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @return 结果
     */
    public static Object multiSearchSync(Cluster cluster, MultiSearchRequest request, ResponseExtractor<?> extractor) {
        return multiSearchAsync(cluster, request, extractor).join();
    }

    /**
     * 异步多搜索
     *
     * @param cluster   集群
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @return 结果
     */
    public static CompletableFuture<Object> multiSearchAsync(Cluster cluster, MultiSearchRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::multiSearchAsync, request, MultiSearchResponse.class, extractor);
    }

    /**
     * 同步搜索
     *
     * @param cluster   客户端
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @return 结果
     */
    public static Object searchSync(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
        return searchAsync(cluster, request, extractor).join();
    }

    /**
     * 异步搜索
     *
     * @param cluster   客户端
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @return 未来结果
     */
    public static CompletableFuture<Object> searchAsync(Cluster cluster, SearchRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::searchAsync, request, SearchResponse.class, extractor);
    }

    /**
     * 异步批量操作
     *
     * @param cluster   客户端
     * @param request   批量请求
     * @param extractor 结果提取器
     * @return 未来结果
     */
    public static CompletableFuture<Object> bulkAsync(Cluster cluster, BulkRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::bulkAsync, request, BulkResponse.class, extractor);
    }

    /**
     * 同步批量操作
     *
     * @param cluster   客户端
     * @param request   批量请求
     * @param extractor 结果提取器
     * @return 结果
     */
    public static Object bulkSync(Cluster cluster, BulkRequest request, ResponseExtractor<?> extractor) {
        return bulkAsync(cluster, request, extractor).join();
    }

    public static Object deleteSync(Cluster cluster, DeleteRequest request, ResponseExtractor<?> extractor) {
        return deleteAsync(cluster, request, extractor).join();
    }

    public static CompletableFuture<Object> deleteAsync(Cluster cluster, DeleteRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::deleteAsync, request, DeleteResponse.class, extractor);
    }

    public static Object deleteByQuerySync(Cluster cluster, DeleteByQueryRequest request, ResponseExtractor<?> extractor) {
        return deleteByQueryAsync(cluster, request, extractor).join();
    }

    public static CompletableFuture<Object> deleteByQueryAsync(Cluster cluster, DeleteByQueryRequest deleteByQueryRequest, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::deleteByQueryAsync, deleteByQueryRequest, BulkByScrollResponse.class, extractor);
    }

    public static Object updateSync(Cluster cluster, UpdateRequest request, ResponseExtractor<?> extractor) {
        return updateAsync(cluster, request, extractor).join();
    }

    public static CompletableFuture<Object> updateAsync(Cluster cluster, UpdateRequest request, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::updateAsync, request, UpdateResponse.class, extractor);
    }

    public static Object updateByQuerySync(Cluster cluster, UpdateByQueryRequest request, ResponseExtractor<?> extractor) {
        return updateByQueryAsync(cluster, request, extractor).join();
    }

    public static CompletableFuture<Object> updateByQueryAsync(Cluster cluster, UpdateByQueryRequest updateByQueryRequest, ResponseExtractor<?> extractor) {
        return performRequestAsync(cluster::updateByQueryAsync, updateByQueryRequest, BulkByScrollResponse.class, extractor);
    }


    private static <T extends ActionRequest, R extends ActionResponse> CompletableFuture<Object> performRequestAsync(BiConsumer<T, ActionListener<R>> consumer, T request, Class<R> responseClass, ResponseExtractor<?> extractor) {
        log.trace("{}", responseClass);
        INTERCEPTOR.preResponse(new DefaultPreResponseInfo<>(request, responseClass, extractor));
        CompletableFuture<Object> future = new CompletableFuture<>();
        if (Env.offlineEnabled()) {
            future.complete(extractor.empty());
            return future;
        }

        consumer.accept(request, wrap(future, extractor, request));
        return future;
    }

    private static <T extends ActionRequest, R extends ActionResponse> ActionListener<R> wrap(CompletableFuture<Object> future,
                                                                                              ResponseExtractor<?> extractor, T request) {
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
                INTERCEPTOR.postResponse(new DefaultPostResponseInfo<>(request, response));
            } finally {
                ContextHolder.remove();
            }
        }, ex -> {
            INTERCEPTOR.handleException(ex);
            future.completeExceptionally(ex);
            ContextHolder.remove();
        });
    }
}

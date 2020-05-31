package com.ymm.ebatis.session;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.domain.Page;
import com.ymm.ebatis.domain.Pageable;
import com.ymm.ebatis.request.CatRequest;
import com.ymm.ebatis.response.ResponseExtractor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.util.concurrent.CompletableFuture;

/**
 * 会话管理
 *
 * @author 章多亮
 * @since 2020/5/23 16:49
 */
public interface ClusterSession extends Cloneable {

    static ClusterSession of(Cluster cluster) {
        return CachedClusterSession.createOrGet(cluster);
    }

    /**
     * 异步GET查询
     *
     * @param request   删除请求
     * @param extractor 响应提取器
     * @return 应县结果
     */
    <T> CompletableFuture<T> getAsync(GetRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步删除
     *
     * @param request   删除请求
     * @param extractor 响应提取器
     * @return 应县结果
     */
    default <T> T getSync(GetRequest request, ResponseExtractor<T> extractor) {
        return getAsync(request, extractor).join();
    }


    /**
     * 异步删除
     *
     * @param request   删除请求
     * @param extractor 响应提取器
     * @return 应县结果
     */
    <T> CompletableFuture<T> deleteAsync(DeleteRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步删除
     *
     * @param request   删除请求
     * @param extractor 响应提取器
     * @return 应县结果
     */
    default <T> T deleteSync(DeleteRequest request, ResponseExtractor<T> extractor) {
        return deleteAsync(request, extractor).join();
    }

    /**
     * 异步查询删除
     *
     * @param request   删除更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 异步结果
     */
    <T> CompletableFuture<T> deleteByQueryAsync(DeleteByQueryRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步查询删除
     *
     * @param request   删除更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 异步结果
     */
    default <T> T deleteByQuerySync(DeleteByQueryRequest request, ResponseExtractor<T> extractor) {
        return deleteByQueryAsync(request, extractor).join();
    }


    /**
     * 异步更新
     *
     * @param request   更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 异步结果
     */
    <T> CompletableFuture<T> updateAsync(UpdateRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步更新
     *
     * @param request   更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 结果
     */
    default <T> T updateSync(UpdateRequest request, ResponseExtractor<T> extractor) {
        return updateAsync(request, extractor).join();
    }

    /**
     * 异步查询更新
     *
     * @param request   查询更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 结果
     */
    <T> CompletableFuture<T> updateByQueryAsync(UpdateByQueryRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步查询更新
     *
     * @param request   查询更新请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 结果
     */
    default <T> T updateByQuerySync(UpdateByQueryRequest request, ResponseExtractor<T> extractor) {
        return updateByQueryAsync(request, extractor).join();
    }

    /**
     * 异步索引
     *
     * @param request   索引请求
     * @param extractor 结果提取器
     * @return 结果
     */
    <T> CompletableFuture<T> indexAsync(IndexRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步索引
     *
     * @param request   索引请求
     * @param extractor 结果提取器
     * @return 结果
     */
    default <T> T indexSync(IndexRequest request, ResponseExtractor<T> extractor) {
        return indexAsync(request, extractor).join();
    }

    /**
     * 异步多搜索
     *
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @return 结果
     */
    <T> CompletableFuture<T> multiSearchAsync(MultiSearchRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步多搜索
     *
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @return 结果
     */
    default <T> T multiSearchSync(MultiSearchRequest request, ResponseExtractor<T> extractor) {
        return multiSearchAsync(request, extractor).join();
    }

    /**
     * 异步多搜索
     *
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @param pageable  分页信息
     * @return 结果
     */
    <T> CompletableFuture<T> multiSearchAsync(MultiSearchRequest request, ResponseExtractor<T> extractor, Pageable[] pageable);

    /**
     * 同步多搜索
     *
     * @param request   多搜索请求
     * @param extractor 响应提取器
     * @param pageable  分页信息
     * @return 结果
     */
    default <T> T multiSearchSync(MultiSearchRequest request, ResponseExtractor<T> extractor, Pageable[] pageable) {
        return multiSearchAsync(request, extractor, pageable).join();
    }

    /**
     * 异步搜索
     *
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 异步结果
     */
    <T> CompletableFuture<T> searchAsync(SearchRequest request, ResponseExtractor<T> extractor);

    /**
     * 同步搜索
     *
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @return 结果
     */
    default <T> T searchSync(SearchRequest request, ResponseExtractor<T> extractor) {
        return searchAsync(request, extractor).join();
    }

    /**
     * 异步搜索
     *
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @param pageable  分页信息
     * @param <T>       结果类型
     * @return 异步结果
     */
    <T> CompletableFuture<Page<T>> searchAsync(SearchRequest request, ResponseExtractor<Page<T>> extractor, Pageable pageable);

    /**
     * 同步搜索
     *
     * @param request   搜索请求
     * @param extractor 结果提取器
     * @param pageable  分页信息
     * @param <T>       结果类型
     * @return 结果
     */
    default <T> Page<T> searchSync(SearchRequest request, ResponseExtractor<Page<T>> extractor, Pageable pageable) {
        return searchAsync(request, extractor, pageable).join();
    }

    /**
     * 同步批量操作
     *
     * @param request   批量请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 结果
     */
    default <T> T bulkSync(BulkRequest request, ResponseExtractor<T> extractor) {
        return bulkAsync(request, extractor).join();
    }

    /**
     * 异步批量操作
     *
     * @param request   批量请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 未来结果
     */
    <T> CompletableFuture<T> bulkAsync(BulkRequest request, ResponseExtractor<T> extractor);

    /**
     * 异步_cat操作
     *
     * @param request   cat请求
     * @param extractor 结果提取器
     * @param <T>       结果类型
     * @return 异步结果
     */
    <T> CompletableFuture<T> catAsync(CatRequest request, ResponseExtractor<T> extractor);

    default <T> T catSync(CatRequest request, ResponseExtractor<T> extractor) {
        return catAsync(request, extractor).join();
    }

    /**
     * 创建或获取指定类型接口代理对象，
     *
     * @param mapperInterface Mapper接口，必须是接口类型
     * @param <T>             接口类型
     * @return 接口映射代理对象
     */
    <T> T getMapper(Class<T> mapperInterface);

    /**
     * 获取当前会话集群
     *
     * @return 集群
     */
    Cluster getCluster();
}

package com.ymm.ebatis.cluster;

import com.ymm.ebatis.request.CatRequest;
import com.ymm.ebatis.response.CatResponse;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.io.IOException;

/**
 * 联邦集群，是为了完成一次写入多个集群，不支持查询
 *
 * @author 章多亮
 * @since 2020/1/16 10:40
 */
public interface FederalCluster extends Cluster {
    /**
     * 由指定的集群列表创建联邦集群
     *
     * @param clusters 集群列表
     * @return 联邦集群
     */
    static FederalCluster of(Cluster... clusters) {
        return new SimpleFederalCluster(clusters);
    }

    /**
     * 联邦集群不支持
     *
     * @return 直接抛出异常
     */
    @Override
    default RestHighLevelClient highLevelClient() {
        throw new UnsupportedOperationException("联邦集群，不支持返回 highLevelClient");
    }

    /**
     * 联邦集群不支持
     *
     * @return 直接抛异常
     */
    @Override
    default RestClient lowLevelClient() {
        throw new UnsupportedOperationException("联邦集群，不支持返回底层 lowLevelClient");
    }

    /**
     * 不支持，直接抛异常
     *
     * @param request  搜索请求
     * @param listener 响应监听器
     */
    @Override
    default void searchAsync(SearchRequest request, ActionListener<SearchResponse> listener) {
        throw new UnsupportedOperationException("联邦集群，不支持 searchAsync");
    }

    /**
     * 不支持，直接抛异常
     *
     * @param request  多搜索请求
     * @param listener 响应监听器
     */
    @Override
    default void multiSearchAsync(MultiSearchRequest request, ActionListener<MultiSearchResponse> listener) {
        throw new UnsupportedOperationException("联邦集群，不支持 multiSearchAsync");
    }

    /**
     * 异步更新
     *
     * @param request  更新请求
     * @param listener 响应监听器
     */
    @Override
    default void updateAsync(UpdateRequest request, ActionListener<UpdateResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.updateAsync(request, listener);
        }
    }

    /**
     * 异步删除
     *
     * @param request  删除请求
     * @param listener 响应监听器
     */
    @Override
    default void deleteAsync(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.deleteAsync(request, listener);
        }
    }

    /**
     * 异步批处理
     *
     * @param request  批处理请求
     * @param listener 响应监听器
     */
    @Override
    default void bulkAsync(BulkRequest request, ActionListener<BulkResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.bulkAsync(request, listener);
        }
    }

    /**
     * 异步索引
     *
     * @param request  索引请求
     * @param listener 响应监听器
     */
    @Override
    default void indexAsync(IndexRequest request, ActionListener<IndexResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.indexAsync(request, listener);
        }
    }

    /**
     * 异步查询删除
     *
     * @param request  查询删除请求
     * @param listener 响应监听器
     */
    @Override
    default void deleteByQueryAsync(DeleteByQueryRequest request, ActionListener<BulkByScrollResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.deleteByQueryAsync(request, listener);
        }
    }

    /**
     * 异步查询更新
     *
     * @param request  查询更新请求
     * @param listener 响应监听器
     */
    @Override
    default void updateByQueryAsync(UpdateByQueryRequest request, ActionListener<BulkByScrollResponse> listener) {
        for (Cluster cluster : getClusters()) {
            cluster.updateByQueryAsync(request, listener);
        }
    }

    @Override
    default void catAsync(CatRequest request, ActionListener<CatResponse> listener) {
        throw new UnsupportedOperationException("联邦集群不支持cat");
    }

    @Override
    default void getAsync(GetRequest request, ActionListener<GetResponse> listener) {
        throw new UnsupportedOperationException("联邦集群不支持GET");
    }

    /**
     * 关闭联邦集群中的所有集群
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    default void close() throws IOException {
        for (Cluster cluster : getClusters()) {
            cluster.close();
        }
    }

    /**
     * 获取一个联邦的所有集群
     *
     * @return 集群列表
     */
    Cluster[] getClusters();
}

package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.request.CatRequest;
import io.manbang.ebatis.core.response.CatResponse;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

import java.io.Closeable;

/**
 * @author 章多亮
 * @since 2019/12/18 9:30
 */
public interface Cluster extends Closeable {
    /**
     * 本地默认配置集群
     *
     * @return 集群
     */
    static Cluster localhost() {
        return localhost(9200);
    }

    static Cluster localhost(int port) {
        return simple("127.0.0.1", port);
    }

    /**
     * 简单集群
     *
     * @param host 主机名
     * @param port 端口号
     * @return 集群
     */
    static Cluster simple(String host, int port) {
        return simple(host, port, null);
    }

    /**
     * 简单集群
     *
     * @param host     主机名
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return 集群
     */
    static Cluster simple(String host, int port, String username, String password) {
        return simple(host, port, Credentials.basic(username, password));
    }

    /**
     * 简单集群
     *
     * @param host        主机名
     * @param port        端口号
     * @param credentials 认证
     * @return 集群
     */
    static Cluster simple(String host, int port, Credentials credentials) {
        return new SimpleCluster(host, port, credentials);
    }

    /**
     * 简单集群
     *
     * @param hosts 主机列表
     * @return 集群
     */
    static Cluster simple(HttpHost[] hosts) {
        return simple(hosts, null);
    }

    /**
     * 简单集群
     *
     * @param hosts    主机列表
     * @param username 用户名
     * @param password 密码
     * @return 集群
     */
    static Cluster simple(HttpHost[] hosts, String username, String password) {
        return simple(hosts, Credentials.basic(username, password));
    }

    /**
     * 简单集群
     *
     * @param hosts       主机列表
     * @param credentials 认证
     * @return 集群
     */
    static Cluster simple(HttpHost[] hosts, Credentials credentials) {
        return new SimpleCluster(hosts, credentials);
    }

    /**
     * 带权集群
     *
     * @param weight 权重
     * @param host   主机名
     * @param port   端口号
     * @return 集群
     */
    static WeightedCluster weighted(int weight, String host, int port) {
        return weighted(weight, host, port, null);
    }

    /**
     * 带权集群
     *
     * @param weight   权重
     * @param host     主机名
     * @param port     端口号
     * @param username 用户名
     * @param password 密码
     * @return 集群
     */
    static WeightedCluster weighted(int weight, String host, int port, String username, String password) {
        return weighted(weight, host, port, Credentials.basic(username, password));
    }

    /**
     * 带权集群
     *
     * @param weight      权重
     * @param host        主机名
     * @param port        端口号
     * @param credentials 认证
     * @return 集群
     */
    static WeightedCluster weighted(int weight, String host, int port, Credentials credentials) {
        return new FixedWeightedCluster(weight, host, port, credentials);
    }

    /**
     * 带权集群
     *
     * @param weight 权重
     * @param hosts  集群节点列表
     * @return 集群
     */
    static WeightedCluster weighted(int weight, HttpHost... hosts) {
        return weighted(weight, hosts, null);
    }

    /**
     * 带权集群
     *
     * @param weight   权重
     * @param hosts    集群节点列表
     * @param username 用户名
     * @param password 密码
     * @return 集群
     */
    static WeightedCluster weighted(int weight, HttpHost[] hosts, String username, String password) {
        return weighted(weight, hosts, Credentials.basic(username, password));
    }

    /**
     * 带权集群
     *
     * @param weight      权重
     * @param hosts       集群节点列表
     * @param credentials 登录凭据
     * @return 集群
     */
    static WeightedCluster weighted(int weight, HttpHost[] hosts, Credentials credentials) {
        return new FixedWeightedCluster(weight, hosts, credentials);
    }

    /**
     * 获取集群的名称，此名字可以用于日志打印，来确定当次ES操作在那个上面
     *
     * @return 集群名称
     */
    String getName();

    /**
     * 从集群种创建或者获取高级客户端
     *
     * @return 高级客户端
     */
    RestHighLevelClient highLevelClient();

    /**
     * 获取底层客户端
     *
     * @return 底层客户端
     */
    RestClient lowLevelClient();

    /**
     * 异步更新
     *
     * @param request  更新请求
     * @param listener 响应监听器
     */
    default void updateAsync(UpdateRequest request, ActionListener<UpdateResponse> listener) {
        highLevelClient().updateAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步删除
     *
     * @param request  删除请求
     * @param listener 响应监听器
     */
    default void deleteAsync(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        highLevelClient().deleteAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步批处理
     *
     * @param request  批处理请求
     * @param listener 响应监听器
     */
    default void bulkAsync(BulkRequest request, ActionListener<BulkResponse> listener) {
        highLevelClient().bulkAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步搜索
     *
     * @param request  搜索请求
     * @param listener 响应监听器
     */
    default void searchAsync(SearchRequest request, ActionListener<SearchResponse> listener) {
        highLevelClient().searchAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步索引
     *
     * @param request  索引请求
     * @param listener 响应监听器
     */
    default void indexAsync(IndexRequest request, ActionListener<IndexResponse> listener) {
        highLevelClient().indexAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步查询删除
     *
     * @param request  查询删除请求
     * @param listener 响应监听器
     */
    default void deleteByQueryAsync(DeleteByQueryRequest request, ActionListener<BulkByScrollResponse> listener) {
        highLevelClient().deleteByQueryAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步查询更新
     *
     * @param request  查询更新请求
     * @param listener 响应监听器
     */
    default void updateByQueryAsync(UpdateByQueryRequest request, ActionListener<BulkByScrollResponse> listener) {
        highLevelClient().updateByQueryAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步多搜索
     *
     * @param request  多搜索请求
     * @param listener 响应监听器
     */
    default void multiSearchAsync(MultiSearchRequest request, ActionListener<MultiSearchResponse> listener) {
        highLevelClient().msearchAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步cat接口
     *
     * @param request  cat请求
     * @param listener 响应监听器
     */
    void catAsync(CatRequest request, ActionListener<CatResponse> listener);

    /**
     * 异步GET请求
     *
     * @param request  Get请求
     * @param listener 响应监听器
     */
    default void getAsync(GetRequest request, ActionListener<GetResponse> listener) {
        highLevelClient().getAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步游标搜索请求
     *
     * @param request  游标请求
     * @param listener 响应监听器
     */
    default void scrollAsync(SearchScrollRequest request, ActionListener<SearchResponse> listener) {
        highLevelClient().scrollAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步执清除游标操作
     *
     * @param request  clear scroll request
     * @param listener response listener
     */
    default void clearScrollAsync(ClearScrollRequest request, ActionListener<ClearScrollResponse> listener) {
        highLevelClient().clearScrollAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 异步多GET请求
     *
     * @param request  multi get request
     * @param listener response listener
     */
    default void mgetAsync(MultiGetRequest request, ActionListener<MultiGetResponse> listener) {
        highLevelClient().mgetAsync(request, RequestOptions.DEFAULT, listener);
    }
}

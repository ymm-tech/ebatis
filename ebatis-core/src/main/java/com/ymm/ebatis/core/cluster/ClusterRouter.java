package com.ymm.ebatis.core.cluster;


import com.ymm.ebatis.core.meta.MethodMeta;

import java.io.Closeable;

/**
 * @author 章多亮
 * @since 2019/12/18 9:30
 */
public interface ClusterRouter extends Closeable {
    /**
     * 本地集群路由，测试用
     *
     * @return 集群路由
     */
    static ClusterRouter localhost() {
        return single(Cluster.localhost());
    }

    /**
     * 创建单集群路由
     *
     * @param cluster 集群
     * @return 集群路由
     */
    static ClusterRouter single(Cluster cluster) {
        return new SingleClusterRouter(cluster);
    }

    /**
     * 轮询算法路由器
     *
     * @param clusters 集群列表
     * @return 集群路由器
     */
    static ClusterRouter roundRobbin(Cluster... clusters) {
        return new RoundRobbinClusterRouter(clusters);
    }

    /**
     * 随机路由器
     *
     * @param clusters 集群列表
     * @return 集群路由
     */
    static ClusterRouter random(Cluster... clusters) {
        return new RandomClusterRouter(clusters);
    }

    /**
     * 权重路由器
     *
     * @param clusters 集群列表
     * @return 集群路由
     */
    static WeightedClusterRouter weighted(WeightedCluster... clusters) {
        return new SimpleWeightedClusterRouter(clusters);
    }

    static ClusterRouter localhost(int port) {
        return single(Cluster.localhost(port));
    }

    /**
     * 根据入参信息选择一个集群执行操作
     *
     * @param meta 方法元信息
     * @return ES集群
     */
    Cluster route(MethodMeta meta);
}

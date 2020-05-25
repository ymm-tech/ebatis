package com.ymm.ebatis.cluster;

/**
 * @author 章多亮
 * @since 2020/1/16 11:56
 */
public interface WeightedClusterRouter extends ClusterRouter {

    /**
     * 获取带权重的集群
     *
     * @return 带权重的集群
     */
    WeightedCluster[] getWeightedClusters();
}

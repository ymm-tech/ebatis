package com.ymm.ebatis.cluster;


import com.ymm.ebatis.meta.MethodMeta;

/**
 * 权重路由器
 *
 * @author 章多亮
 * @since 2019/12/18 10:37
 */
public abstract class AbstractWeightedClusterRouter extends AbstractClusterRouter implements WeightedClusterRouter {
    @Override
    protected Cluster[] getClusters() {
        return getWeightedClusters();
    }

    @Override
    protected ClusterLoadBalancer getLoadBalancer(MethodMeta meta) {
        return ClusterLoadBalancer.weighted();
    }
}

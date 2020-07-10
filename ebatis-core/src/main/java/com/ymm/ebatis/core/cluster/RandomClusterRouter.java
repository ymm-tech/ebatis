package com.ymm.ebatis.core.cluster;

/**
 * @author 章多亮
 * @since 2019/12/18 14:05
 */
class RandomClusterRouter extends SimpleClusterRouter {
    public RandomClusterRouter(Cluster[] clusters) {
        super(clusters, ClusterLoadBalancer.random());
    }
}

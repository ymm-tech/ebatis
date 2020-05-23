package com.ymm.ebatis.core.cluster;

/**
 * @author 章多亮
 * @since 2019/12/18 14:07
 */
class RoundRobbinClusterRouter extends SimpleClusterRouter {
    public RoundRobbinClusterRouter(Cluster[] clusters) {
        super(clusters, ClusterLoadBalancer.roundRobbin());
    }
}

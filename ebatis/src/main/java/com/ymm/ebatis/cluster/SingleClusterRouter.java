package com.ymm.ebatis.cluster;

import com.ymm.ebatis.meta.MethodMeta;

/**
 * @author 章多亮
 * @since 2019/12/19 18:58
 */
class SingleClusterRouter extends AbstractClusterRouter {
    private final Cluster[] clusters;

    public SingleClusterRouter(Cluster cluster) {
        this.clusters = new Cluster[]{cluster};
    }

    @Override
    protected Cluster[] getClusters() {
        return clusters;
    }

    @Override
    protected ClusterLoadBalancer getLoadBalancer(MethodMeta meta) {
        return SingleClusterLoaderBalancer.INSTANCE;
    }
}

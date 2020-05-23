package com.ymm.ebatis.core.cluster;

/**
 * @author 章多亮
 * @since 2019/12/19 18:59
 */
class SingleClusterLoaderBalancer implements ClusterLoadBalancer {
    static final SingleClusterLoaderBalancer INSTANCE = new SingleClusterLoaderBalancer();

    private SingleClusterLoaderBalancer() {
    }

    @Override
    public Cluster choose(Cluster[] clusters) {
        return clusters[0];
    }
}

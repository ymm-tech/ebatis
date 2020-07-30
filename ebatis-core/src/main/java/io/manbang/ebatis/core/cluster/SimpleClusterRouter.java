package io.manbang.ebatis.core.cluster;


import io.manbang.ebatis.core.meta.MethodMeta;

/**
 * @author 章多亮
 * @since 2019/12/18 14:03
 */
public class SimpleClusterRouter extends AbstractClusterRouter {
    private final Cluster[] clusters;
    private final ClusterLoadBalancer loadBalancer;

    public SimpleClusterRouter(Cluster[] clusters, ClusterLoadBalancer loadBalancer) {
        this.clusters = clusters;
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected Cluster[] getClusters() {
        return clusters;
    }

    @Override
    protected ClusterLoadBalancer getLoadBalancer(MethodMeta meta) {
        return loadBalancer;
    }
}

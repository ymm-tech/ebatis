package com.ymm.ebatis.cluster;

/**
 * @author 章多亮
 * @since 2019/12/18 14:06
 */
class SimpleWeightedClusterRouter extends AbstractWeightedClusterRouter {
    private final WeightedCluster[] clusters;

    public SimpleWeightedClusterRouter(WeightedCluster[] clusters) {
        this.clusters = clusters;
    }

    @Override
    public WeightedCluster[] getWeightedClusters() {
        return clusters;
    }
}

package io.manbang.ebatis.core.cluster;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2019/12/18 10:30
 */
class SimpleWeightedClusterLoadBalancer extends AbstractWeightedClusterLoadBalancer {
    static final SimpleWeightedClusterLoadBalancer INSTANCE = new SimpleWeightedClusterLoadBalancer();
    /**
     * 禁用的集群权重值
     */
    private static final int DISABLED_CLUSTER_WEIGHT = 0;

    private SimpleWeightedClusterLoadBalancer() {
    }

    @Override
    protected Cluster doChoose(WeightedCluster[] clusters) {
        // 得到所有集群的总权重值，权重值越大，在总权重种占有的比值就越大；随机数本身是随机的，随机数落在权重大的集群几率就越大。
        int totalWeight = Stream.of(clusters).mapToInt(Weighted::getWeight).filter(this::isEnabled).sum();
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight);
        //假设权重2,4,6,site:[0,12),对应权重区间:[0,2) [2,6) [6,12)
        for (WeightedCluster cluster : clusters) {
            if ((randomWeight = randomWeight - cluster.getWeight()) < 0) {
                return cluster;
            }
        }
        return clusters[0];
    }

    /**
     * 判断集群是否启用，如果权重小于或等于0，则集群禁用
     *
     * @param weight 权重
     * @return 如果启用，返回<code>true</code>，否则，返回<code>false</code>
     */
    private boolean isEnabled(int weight) {
        return weight > DISABLED_CLUSTER_WEIGHT;
    }
}

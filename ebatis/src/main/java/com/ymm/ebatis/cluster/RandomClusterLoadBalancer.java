package com.ymm.ebatis.cluster;

import java.util.Random;

/**
 * @author 章多亮
 * @since 2019/12/18 10:19
 */
class RandomClusterLoadBalancer extends AbstractClusterLoadBalancer {
    static final RandomClusterLoadBalancer INSTANCE = new RandomClusterLoadBalancer();
    /**
     * 集群下标随机生成器，利用ThreadLocal来防止多线程争用，导致随机数生成的性能劣化
     */
    private final ThreadLocal<Random> random = ThreadLocal.withInitial(Random::new);

    private RandomClusterLoadBalancer() {
    }

    @Override
    protected Cluster doChoose(Cluster[] clusters) {
        return clusters[random.get().nextInt(clusters.length)];
    }
}

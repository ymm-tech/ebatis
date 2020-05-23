package com.ymm.ebatis.core.cluster;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 章多亮
 * @since 2019/12/18 10:16
 */
class RoundRobinClusterLoadBalancer extends AbstractClusterLoadBalancer {
    public static final RoundRobinClusterLoadBalancer INSTANCE = new RoundRobinClusterLoadBalancer();
    private final ThreadLocal<AtomicInteger> index = ThreadLocal.withInitial(AtomicInteger::new);

    private RoundRobinClusterLoadBalancer() {
    }

    @Override
    protected Cluster doChoose(Cluster[] clusters) {
        return clusters[index.get().getAndIncrement() % clusters.length];
    }
}

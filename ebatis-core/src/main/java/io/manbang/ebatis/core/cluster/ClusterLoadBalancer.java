package io.manbang.ebatis.core.cluster;

/**
 * 集群负责均衡器
 *
 * @author 章多亮
 * @since 2019/12/18 9:31
 */
public interface ClusterLoadBalancer {
    /**
     * 随机负载均衡器
     *
     * @return 负载均衡
     */
    static ClusterLoadBalancer random() {
        return RandomClusterLoadBalancer.INSTANCE;
    }

    /**
     * 获取轮询负载均衡器
     *
     * @return 负载均衡
     */
    static ClusterLoadBalancer roundRobbin() {
        return RoundRobinClusterLoadBalancer.INSTANCE;
    }

    /**
     * 获取带权负载均衡器
     *
     * @return 带权负载均衡器
     */
    static WeightedClusterLoadBalancer weighted() {
        return SimpleWeightedClusterLoadBalancer.INSTANCE;
    }

    /**
     * 负载均衡器从一组集群种选择一个集群
     *
     * @param clusters 集群列表
     * @return 选定的集群
     */
    Cluster choose(Cluster[] clusters);
}

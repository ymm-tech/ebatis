package com.ymm.ebatis.cluster;

import com.ymm.ebatis.exception.NoAvailableClusterException;

/**
 * @author 章多亮
 * @since 2020/1/16 11:45
 */
public abstract class AbstractWeightedClusterLoadBalancer implements WeightedClusterLoadBalancer {
    /**
     * 负载均衡器从一组集群种选择一个集群
     *
     * @param clusters 集群列表
     * @return 选定的集群
     */
    @Override
    public Cluster choose(Cluster[] clusters) {
        // 如果没有集群实例，直接抛出异常
        if (clusters == null || clusters.length == 0) {
            throw new NoAvailableClusterException();
        }

        // 如果只有一个集群，直接选择，不需要经过负载
        if (clusters.length == 1) {
            return clusters[0];
        }

        return doChoose((WeightedCluster[]) clusters);
    }

    /**
     * 从带权集群中选择选择一集群
     *
     * @param clusters 带权集群
     * @return 集群
     */
    protected abstract Cluster doChoose(WeightedCluster[] clusters);
}

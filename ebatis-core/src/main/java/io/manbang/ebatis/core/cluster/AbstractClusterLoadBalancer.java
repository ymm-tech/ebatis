package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.exception.NoAvailableClusterException;

/**
 * @author 章多亮
 * @since 2019/12/18 11:11
 */
public abstract class AbstractClusterLoadBalancer implements ClusterLoadBalancer {
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

        return doChoose(clusters);
    }

    /**
     * 多个集群的情况下，必须做出集群选择
     *
     * @param clusters 集群列表
     * @return 选中的集群
     */
    protected abstract Cluster doChoose(Cluster[] clusters);
}

package com.ymm.ebatis.core.cluster;

import java.util.StringJoiner;

/**
 * @author 章多亮
 * @since 2020/1/16 11:08
 */
class SimpleFederalCluster implements FederalCluster {
    private final Cluster[] clusters;
    private final String name;

    SimpleFederalCluster(Cluster... clusters) {
        this.clusters = clusters;

        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (Cluster cluster : clusters) {
            joiner.add(cluster.getName());
        }
        this.name = joiner.toString();
    }


    /**
     * 获取一个联邦的所有集群
     *
     * @return 集群列表
     */
    @Override
    public Cluster[] getClusters() {
        return clusters;
    }

    /**
     * 获取集群的名称，此名字可以用于日志打印，来确定当次ES操作在那个上面
     *
     * @return 集群名称
     */
    @Override
    public String getName() {
        return name;
    }
}

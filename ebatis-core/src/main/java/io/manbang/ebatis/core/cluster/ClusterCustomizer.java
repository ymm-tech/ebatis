package io.manbang.ebatis.core.cluster;

/**
 * 集群定制器
 *
 * @author 章多亮
 * @since 2019/12/18 14:21
 */
public interface ClusterCustomizer {
    /**
     * 定制集群
     *
     * @param cluster ES集群
     */
    void custom(Cluster cluster);
}

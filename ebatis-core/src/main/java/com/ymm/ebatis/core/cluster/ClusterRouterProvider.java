package com.ymm.ebatis.core.cluster;

import com.ymm.ebatis.core.annotation.Mapper;

/**
 * 集群路由提供方，名字是唯一标识，通过配置 {@link Mapper#clusterRouter()}，定制每个Mapper的集群路由
 *
 * @author 章多亮
 * @since 2020/6/2 18:23
 */
@FunctionalInterface
public interface ClusterRouterProvider {
    /**
     * 集群路由名称
     *
     * @return 名称
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 获取指定名称的集群路由器
     *
     * @return 集群路由器
     */
    ClusterRouter getClusterRouter();
}

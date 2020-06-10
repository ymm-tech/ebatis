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
     * 找到对应集群提供方后，会回调此接口，默认啥都不干
     */
    default void init() {
        // do nothing
    }

    /**
     * 获取指定名称的集群路由器
     *
     * @param name 集群名称
     * @return 集群路由器
     */
    ClusterRouter getClusterRouter(String name);
}

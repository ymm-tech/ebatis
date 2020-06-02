package com.ymm.ebatis.core.cluster;

import com.google.auto.service.AutoService;

/**
 * @author 章多亮
 * @since 2020/6/2 18:23
 */
@AutoService(ClusterRouterProvider.class)
public class LocalhostClusterRouterProvider implements ClusterRouterProvider {
    @Override
    public String getName() {
        return "localhost";
    }

    @Override
    public ClusterRouter getClusterRouter() {
        return ClusterRouter.localhost();
    }
}

package com.ymm.ebatis.spring.cluster;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterProvider;

/**
 * @author 章多亮
 * @since 2020/6/3 10:37
 */
@AutoService(ClusterRouterProvider.class)
public class SpringClusterRouterProvider implements ClusterRouterProvider {
    @Override
    public String getName() {
        return "clusterRouter";
    }

    @Override
    public ClusterRouter getClusterRouter() {
        return ApplicationContextHolder.getBean(getName(), ClusterRouter.class);
    }
}

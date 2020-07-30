package io.manbang.ebatis.core.cluster;

import com.google.auto.service.AutoService;

/**
 * @author 章多亮
 * @since 2020/6/2 18:23
 */
@AutoService(ClusterRouterProvider.class)
public class LocalhostClusterRouterProvider implements ClusterRouterProvider {
    private static final String CLUSTER_ROUTER_NAME = "localhost";

    @Override
    public ClusterRouter getClusterRouter(String name) {
        if (CLUSTER_ROUTER_NAME.equalsIgnoreCase(name)) {
            return ClusterRouter.localhost();
        } else {
            return null;
        }
    }
}

package io.manbang.ebatis.core.cluster;

import io.manbang.ebatis.core.exception.ClusterRouterNotFoundException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由器载入器，对名字缓存
 *
 * @author 章多亮
 * @since 2020/6/2 17:43
 */
@Slf4j
public class ClusterRouterLoader {
    private static final Map<String, ClusterRouter> CLUSTER_ROUTERS = new ConcurrentHashMap<>();

    private ClusterRouterLoader() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取指定集群路由器名称的集群路由
     *
     * @param name 集群路由名称
     * @return 集群路由器
     */
    public static ClusterRouter getClusterRouter(String name) {
        return CLUSTER_ROUTERS.computeIfAbsent(name, ClusterRouterLoader::findClusterRouter);
    }

    /**
     * 从SPI中获取指定名称的集群路由器，名字是集群路由提供方的唯一标识，如果有重复，那就是取第一个匹配的
     *
     * @param name 集群路由名称
     * @return 集群路由器
     */
    @Synchronized
    private static ClusterRouter findClusterRouter(String name) {
        ServiceLoader<ClusterRouterProvider> providers = ServiceLoader.load(ClusterRouterProvider.class);

        for (ClusterRouterProvider provider : providers) {
            ClusterRouter clusterRouter = provider.getClusterRouter(name);
            if (clusterRouter != null) {
                provider.init();

                log.info("选定集群路由：[{}] {}", name, provider.getClass());

                return clusterRouter;
            }
        }

        throw new ClusterRouterNotFoundException(name);
    }
}

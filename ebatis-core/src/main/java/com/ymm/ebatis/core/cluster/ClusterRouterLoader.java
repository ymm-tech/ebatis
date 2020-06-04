package com.ymm.ebatis.core.cluster;

import com.ymm.ebatis.core.exception.ClusterRouterNotFoundException;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * 路由器载入器，对名字缓存
 *
 * @author 章多亮
 * @since 2020/6/2 17:43
 */
@Slf4j
public class ClusterRouterLoader {
    private static final Map<String, ClusterRouter> CLUSTER_ROUTERS = new HashMap<>();

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
            if (Objects.equals(name, provider.getName())) {
                provider.init();

                log.info("选定集群路由：[{}] {}", name, provider.getClass());

                return provider.getClusterRouter();
            }
        }

        throw new ClusterRouterNotFoundException(name);
    }
}

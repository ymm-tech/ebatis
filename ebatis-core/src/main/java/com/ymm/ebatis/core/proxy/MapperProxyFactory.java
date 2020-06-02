package com.ymm.ebatis.core.proxy;

import com.ymm.ebatis.core.config.Env;

/**
 * @author 章多亮
 * @since 2020/5/25 17:28
 */
public interface MapperProxyFactory {
    /**
     * 创建接口，如果指定的映射接口已经存在，则直接返回缓存
     *
     * @param mapperInterface   映射接口
     * @param classLoader       类加载器
     * @param clusterRouterName 集群路由
     * @param <M>               接口类型
     * @return 接口代理
     */
    static <M> M getMapperProxy(Class<M> mapperInterface, ClassLoader classLoader, String clusterRouterName) {
        return MapperRegistry.createIfAbsent(mapperInterface, classLoader, clusterRouterName);
    }

    /**
     * 创建接口，如果指定的映射接口已经存在，则直接返回缓存
     *
     * @param mapperInterface   映射接口
     * @param clusterRouterName 集群路由
     * @param <M>               接口类型
     * @return 接口代理
     */
    static <M> M getMapperProxy(Class<M> mapperInterface, String clusterRouterName) {
        return getMapperProxy(mapperInterface, null, clusterRouterName);
    }

    /**
     * 创建接口，如果指定的映射接口已经存在，则直接返回缓存
     *
     * @param mapperInterface 映射接口
     * @param <M>             接口类型
     * @return 接口代理
     */
    static <M> M getMapperProxy(Class<M> mapperInterface) {
        return getMapperProxy(mapperInterface, mapperInterface.getClassLoader(), Env.getClusterRouter());
    }
}

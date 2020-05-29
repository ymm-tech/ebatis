package com.ymm.ebatis.proxy;

import com.ymm.ebatis.annotation.Mapper;
import com.ymm.ebatis.cluster.ClusterRouter;
import com.ymm.ebatis.exception.MapperAnnotationNotPresentException;
import com.ymm.ebatis.exception.MapperNotAllowInheritException;
import com.ymm.ebatis.exception.MapperNotInterfaceException;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 章多亮
 * @since 2020/5/25 17:27
 */
class MapperRegistry {
    private static final Map<Class<?>, Object> PROXIES = new HashMap<>();

    private MapperRegistry() {
        throw new UnsupportedOperationException();
    }

    static synchronized Object createProxy(Class<?> mapperInterface, ClassLoader classLoader, ClusterRouter router) {
        ClassLoader cl = classLoader == null ? mapperInterface.getClassLoader() : classLoader;
        return Proxy.newProxyInstance(cl, new Class[]{mapperInterface}, new MapperProxy(mapperInterface, router));
    }

    @SuppressWarnings("unchecked")
    static <M> M createIfAbsent(Class<M> mapperInterface, ClassLoader classLoader, ClusterRouter router) {
        if (!mapperInterface.isInterface()) {
            throw new MapperNotInterfaceException(mapperInterface.toString());
        }

        if (mapperInterface.getInterfaces().length > 0) {
            throw new MapperNotAllowInheritException(mapperInterface.toString());
        }

        if (!mapperInterface.isAnnotationPresent(Mapper.class)) {
            throw new MapperAnnotationNotPresentException(mapperInterface.toString());
        }

        return (M) PROXIES.computeIfAbsent(mapperInterface, clazz -> createProxy(clazz, classLoader, router));
    }
}

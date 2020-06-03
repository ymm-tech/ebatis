package com.ymm.ebatis.core.proxy;

import com.ymm.ebatis.core.annotation.Mapper;
import com.ymm.ebatis.core.exception.MapperAnnotationNotPresentException;
import com.ymm.ebatis.core.exception.MapperNotAllowInheritException;
import com.ymm.ebatis.core.exception.MapperNotInterfaceException;

import java.lang.annotation.Annotation;
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

    private static synchronized Object createProxy(Class<?> mapperInterface, ClassLoader classLoader, String clusterRouterName) {
        ClassLoader cl = classLoader == null ? mapperInterface.getClassLoader() : classLoader;
        return Proxy.newProxyInstance(cl, new Class[]{mapperInterface}, new MapperProxy(mapperInterface, clusterRouterName));
    }

    @SuppressWarnings("unchecked")
    static <M> M createIfAbsent(Class<M> mapperInterface, ClassLoader classLoader, String clusterRouterName) {
        if (!mapperInterface.isInterface()) {
            throw new MapperNotInterfaceException(mapperInterface.toString());
        }

        if (mapperInterface.getInterfaces().length > 0) {
            throw new MapperNotAllowInheritException(mapperInterface.toString());
        }

        Annotation[] annotations = mapperInterface.getAnnotations();

        boolean mapperPresent = false;
        for (Annotation annotation : annotations) {
            mapperPresent = annotation.annotationType() == Mapper.class || annotation.annotationType().isAnnotationPresent(Mapper.class);
            if (mapperPresent) {
                break;
            }
        }

        if (!mapperPresent) {
            throw new MapperAnnotationNotPresentException(mapperInterface.toString());
        }


        return (M) PROXIES.computeIfAbsent(mapperInterface, clazz -> createProxy(clazz, classLoader, clusterRouterName));
    }
}

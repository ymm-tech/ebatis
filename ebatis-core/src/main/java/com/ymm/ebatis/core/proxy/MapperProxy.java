package com.ymm.ebatis.core.proxy;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterLoader;
import com.ymm.ebatis.core.domain.Context;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.meta.MapperInterface;
import com.ymm.ebatis.core.meta.MapperMethod;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/22 15:04
 */
class MapperProxy implements InvocationHandler {
    private final MapperInterface mapperInterface;
    private final ClusterRouter clusterRouter;

    MapperProxy(Class<?> mapperInterface, String name) {
        this.clusterRouter = ClusterRouterLoader.getClusterRouter(name);
        this.mapperInterface = MapperInterface.of(mapperInterface);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Context context = ContextHolder.getContext();

        context.getStopWatch().ifPresent(stopWatch -> {
            stopWatch.stop();
            stopWatch.start("查询准备耗时");
        });

        if (ReflectionUtils.isObjectMethod(method)) {
            return invokeObjectMethod(method, args);
        }

        MapperMethod mapperMethod = mapperInterface.getMapperMethod(method);
        ContextHolder.setHttpConfig(mapperMethod.getHttpConfig());

        Cluster cluster = clusterRouter.route(mapperMethod);

        return mapperMethod.invoke(cluster, args);
    }

    private Object invokeObjectMethod(Method method, Object[] args) {
        if (ReflectionUtils.isToStringMethod(method)) {
            return mapperInterface.toString();
        } else if (ReflectionUtils.isHashCodeMethod(method)) {
            return mapperInterface.hashCode();
        } else if (ReflectionUtils.isEqualsMethod(method)) {
            return mapperInterface.equals(args[0]);
        } else {
            return null;
        }
    }
}

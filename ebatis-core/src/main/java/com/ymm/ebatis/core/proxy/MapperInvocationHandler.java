package com.ymm.ebatis.core.proxy;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.meta.MapperClassMeta;
import com.ymm.ebatis.core.meta.MethodMeta;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/22 15:04
 */
public class MapperInvocationHandler implements InvocationHandler {

    private final ClusterRouter clusterRouter;
    private final MapperClassMeta classMeta;

    MapperInvocationHandler(Class<?> mapperClass, ClusterRouter clusterRouter) {
        this.clusterRouter = clusterRouter;
        this.classMeta = MapperClassMeta.from(mapperClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        ContextHolder.getContext().getStopWatch().ifPresent(stopWatch -> {
            stopWatch.stop();
            stopWatch.start("查询准备耗时");
        });

        if (ReflectionUtils.isObjectMethod(method)) {
            return invokeObjectMethod(method, args);
        }

        Cluster cluster = clusterRouter.route(MethodMeta.from(method));
        MethodMeta mapperMethod = classMeta.getMethodMeta(method);

        // TODO 获取方法执行器，然后执行ES请求
        return null;
    }

    private Object invokeObjectMethod(Method method, Object[] args) {
        if (ReflectionUtils.isToStringMethod(method)) {
            return classMeta.toString();
        } else if (ReflectionUtils.isHashCodeMethod(method)) {
            return classMeta.hashCode();
        } else if (ReflectionUtils.isEqualsMethod(method)) {
            return classMeta.equals(args[0]);
        } else {
            return null;
        }
    }
}

package com.ymm.ebatis.core.proxy;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.cluster.ClusterRouter;
import com.ymm.ebatis.core.cluster.ClusterRouterLoader;
import com.ymm.ebatis.core.config.Env;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.meta.MapperInterface;
import com.ymm.ebatis.core.meta.MapperMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper接口的代理，解析接口定义的元数据信息，并将元数据和实参转变成对应请求调用
 *
 * @author 章多亮
 * @since 2020/5/22 15:04
 */
class MapperProxy implements InvocationHandler {
    private final MapperInterface mapperInterface;
    private final String clusterRouterName;
    private volatile ClusterRouter clusterRouter; // NOSONAR

    MapperProxy(Class<?> mapperType, String name) {
        this.mapperInterface = MapperInterface.of(mapperType);
        this.clusterRouterName = getClusterRouterName(name);
    }

    /**
     * 有好几个地方，可以获取集群路由名称，优先级最高是传入的名称，其次是接口注解 clusterRouter属性定义的名称，最后是ebatis.properties中配置的名称
     *
     * @param name 手动传入名字
     * @return 集群路由名称
     */
    private String getClusterRouterName(String name) {
        // 传入的名称优先级最高，不为空的话，直接返回
        if (StringUtils.isNotBlank(name)) {
            return name;
        }

        // 其次是注解在接口的属性定义
        String n = mapperInterface.getClusterRouterName();
        if (StringUtils.isNotBlank(n)) {
            return n;
        }

        // 最后从配置文件中读取
        return Env.getClusterRouterName();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (ReflectionUtils.isObjectMethod(method)) {
            return invokeObjectMethod(method, args);
        }

        MapperMethod mapperMethod = mapperInterface.getMapperMethod(method);
        ContextHolder.setHttpConfig(mapperMethod.getHttpConfig());

        Cluster cluster = getClusterRouter().route(mapperMethod);

        return mapperMethod.invoke(cluster, args);
    }

    private ClusterRouter getClusterRouter() {
        if (clusterRouter == null) {
            synchronized (this) {
                if (clusterRouter == null) {
                    clusterRouter = ClusterRouterLoader.getClusterRouter(clusterRouterName);
                }
            }
        }

        return clusterRouter;
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

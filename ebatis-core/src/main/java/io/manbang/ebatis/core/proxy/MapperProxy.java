package io.manbang.ebatis.core.proxy;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.cluster.ClusterRouterLoader;
import io.manbang.ebatis.core.common.MethodUtils;
import io.manbang.ebatis.core.config.Env;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.exception.MethodInvokeException;
import io.manbang.ebatis.core.meta.MapperInterface;
import io.manbang.ebatis.core.meta.MapperMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.elasticsearch.common.collect.Tuple;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper接口的代理，解析接口定义的元数据信息，并将元数据和实参转变成对应请求调用
 *
 * @author 章多亮
 * @since 2020/5/22 15:04
 */
class MapperProxy implements InvocationHandler {
    private final MapperInterface mapperInterface;
    private final String clusterRouterName;
    private final LazyInitializer<ClusterRouter> clusterRouter;
    private final Map<? extends Class<?>, Object> mapperInstances;

    MapperProxy(Class<?> mapperType, String name) {
        this.mapperInterface = MapperInterface.of(mapperType);
        this.clusterRouterName = getClusterRouterName(name);
        this.clusterRouter = new LazyInitializer<ClusterRouter>() {
            @Override
            protected ClusterRouter initialize() {
                return ClusterRouterLoader.getClusterRouter(clusterRouterName);
            }
        };
        final Class<?>[] interfaces = mapperType.getInterfaces();
        this.mapperInstances = Arrays.stream(interfaces).map(i -> Tuple.tuple(i, MapperType.type(i).instance(clusterRouter))).collect(Collectors.toMap(Tuple::v1, Tuple::v2));
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
    public Object invoke(Object proxy, Method method, Object[] args) throws ConcurrentException {
        if (MethodUtils.isObjectMethod(method)) {
            return invokeObjectMethod(method, args);
        }

        if (isMapperMethod(method)) {
            return invokeMapperMethod(method, args);
        }

        MapperMethod mapperMethod = mapperInterface.getMapperMethod(method);
        ContextHolder.setHttpConfig(mapperMethod.getHttpConfig());

        Cluster cluster = clusterRouter.get().route(mapperMethod);

        return mapperMethod.invoke(cluster, args);
    }

    private Object invokeObjectMethod(Method method, Object[] args) {
        if (MethodUtils.isToStringMethod(method)) {
            return mapperInterface.toString();
        } else if (MethodUtils.isHashCodeMethod(method)) {
            return mapperInterface.hashCode();
        } else if (MethodUtils.isEqualsMethod(method)) {
            return mapperInterface.equals(args[0]);
        } else {
            return null;
        }
    }

    private Object invokeMapperMethod(Method method, Object[] args) {
        try {
            return method.invoke(mapperInstances.get(method.getDeclaringClass()), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MethodInvokeException(e);
        }
    }

    private boolean isMapperMethod(Method method) {
        return mapperInstances.containsKey(method.getDeclaringClass());
    }
}

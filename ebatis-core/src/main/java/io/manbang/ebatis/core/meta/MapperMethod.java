package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.cluster.Cluster;

import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/27 18:11
 */
public interface MapperMethod extends MethodMeta {

    /**
     * create mapper method
     *
     * @param mapperInterface mapper interface
     * @param method          method of mapper interface
     * @return mapper method
     */
    static MapperMethod of(MapperInterface mapperInterface, Method method) {
        return new DefaultMapperMethodMeta(mapperInterface, method);
    }

    /**
     * 调用映射方法
     *
     * @param cluster 集群
     * @param args    请求实参
     * @return 响应结果
     */
    Object invoke(Cluster cluster, Object[] args);
}

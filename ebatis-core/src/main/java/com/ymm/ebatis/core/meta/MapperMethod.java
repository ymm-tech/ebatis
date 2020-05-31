package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.cluster.Cluster;

import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/27 18:11
 */
public interface MapperMethod extends MethodMeta {

    static MapperMethod of(MapperInterface mapperInterface, Method method) {
        return new DefaultMapperMethodMeta(mapperInterface, method);
    }

    Object execute(Cluster cluster, Object[] args);
}

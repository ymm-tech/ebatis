package com.ymm.ebatis.meta;

import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/27 17:08
 */
public interface MapperInterface extends AnnotatedMeta<Class<?>> {
    static <T> MapperInterface of(Class<T> mapperInterface) {
        return new DefaultMapperInterface(mapperInterface);
    }

    String[] getIndices();

    String[] getTypes();

    String getRouting();

    String getClusterRouter();

    MapperMethod getMapperMethod(Method method);
}

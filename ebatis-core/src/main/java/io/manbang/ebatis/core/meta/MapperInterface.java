package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.domain.HttpConfig;

import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/5/27 17:08
 */
public interface MapperInterface extends AnnotatedMeta<Class<?>> {
    /**
     * 缓存创建指定该接口的映射
     *
     * @param mapperType 映射接口
     * @param <T>        实际接口类型
     * @return 接口映射信息
     */
    static <T> MapperInterface of(Class<T> mapperType) {
        return new DefaultMapperInterface(mapperType);
    }

    /**
     * 获取此接口要操作的索引
     *
     * @return 索引列表
     */
    String[] getIndices();

    /**
     * 获取此借口遥操作的索引列表
     *
     * @return 类型列表
     */
    String[] getTypes();

    /**
     * 获取集群路由器
     *
     * @return 集群路由
     */
    String getClusterRouterName();

    /**
     * 一些http相关的配置信息，连接ES
     *
     * @return http配置
     */
    HttpConfig getHttpConfig();

    /**
     * 获取指定方法的映射方法
     *
     * @param method 接口 方法
     * @return 映射方法
     */
    MapperMethod getMapperMethod(Method method);
}

package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Mapper;

import java.lang.reflect.Method;

/**
 * 映射接口元数据
 *
 * @author 章多亮
 * @since 2020/5/22 15:31
 */
public interface MapperClassMeta extends AnnotatedMeta<Class<?>> {

    /**
     * 创建映射接口元数据
     *
     * @param mapperClass 映射接口
     * @return 接口元数据
     */
    static MapperClassMeta from(Class<?> mapperClass) {
        return new DefaultMapperClassMeta(mapperClass);
    }

    /**
     * 获取被注解的映射接口类型
     *
     * @return 类
     */
    @Override
    default Class<?> getElement() {
        return getType();
    }

    /**
     * 全局路由信息
     *
     * @return 路由信息
     */
    String[] routing();

    /**
     * 一个接口的定义，会确定ES索引
     *
     * @return 索引名称
     */
    String[] indices();

    /**
     * 一个接口的定义，会确定ES类型Mapping
     *
     * @return 类型名称
     */
    String[] types();

    /**
     * 获取指定方法的元数据信息
     *
     * @param method 方法
     * @return 方法元数据
     */
    MethodMeta getMethodMeta(Method method);

    /**
     * 获取类的映射注解，这个注解对应着ES的索引定义
     *
     * @return 映射注解
     */
    Mapper getMapper();
}

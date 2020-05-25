package com.ymm.ebatis.meta;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

/**
 * 方法参数元数据，确定参数的顺序
 *
 * @author 章多亮
 * @since 2020/5/21 14:29
 */
public interface ParameterMeta extends AnnotatedMeta<Parameter> {

    /**
     * 创建带索引的形参元数据
     *
     * @param index     形参索引
     * @param parameter 形参
     * @return 形参元数据
     */
    static ParameterMeta withIndex(int index, Parameter parameter) {
        return new DefaultParameterMeta(index, parameter);
    }

    /**
     * 注解元素就是形参
     *
     * @return 形参
     */
    @Override
    default Parameter getElement() {
        return getParameter();
    }

    /**
     * 获取形参
     *
     * @return 形参
     */
    Parameter getParameter();

    /**
     * 获取参数所属的方法
     *
     * @return 方法
     */
    default Method getDeclaringMethod() {
        return (Method) getParameter().getDeclaringExecutable();
    }

    /**
     * 获取形参的索引，用于查找实参
     *
     * @return 形参索引
     */
    int getIndex();

    /**
     * 获取形参类型
     *
     * @return 参数类型
     */
    @Override
    default Class<?> getType() {
        return getParameter().getType();
    }

    /**
     * 获取泛型参数类型
     *
     * @return 泛型参数类型
     */
    default ParameterizedType getParameterizedType() {
        return (ParameterizedType) getParameter().getParameterizedType();
    }
}

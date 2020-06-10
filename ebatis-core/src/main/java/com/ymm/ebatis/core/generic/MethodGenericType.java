package com.ymm.ebatis.core.generic;

import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/6/9 11:06
 */
public interface MethodGenericType extends GenericType {
    /**
     * 泛型解析的防范
     *
     * @return 方法
     */
    Method getMethod();

    /**
     * 获取方法的返回值泛型解析器
     *
     * @return 泛型解析器
     */
    GenericType returnType();

    /**
     * 获取指定参数位置的泛型解析器
     *
     * @param index 参数索引
     * @return 泛型解析器
     */
    GenericType parameterType(int index);
}

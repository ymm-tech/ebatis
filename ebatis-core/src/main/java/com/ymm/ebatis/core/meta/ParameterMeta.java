package com.ymm.ebatis.core.meta;

import java.lang.reflect.Parameter;

/**
 * @author 章多亮
 * @since 2020/5/27 18:53
 */
public interface ParameterMeta extends AnnotatedMeta<Parameter>, ConditionMeta {
    static ParameterMeta withIndex(MethodMeta methodMeta, Parameter parameter, int index) {
        return new DefaultParameterMeta(methodMeta, parameter, index);
    }

    boolean isPageable();

    boolean isResponseExtractor();

    int getIndex();

    @Override
    Class<?> getType();

    Object getValue(Object[] args);
}

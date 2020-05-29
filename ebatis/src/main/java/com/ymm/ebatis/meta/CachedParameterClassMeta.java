package com.ymm.ebatis.meta;

import com.ymm.ebatis.exception.EbatisException;

import java.lang.reflect.Parameter;

/**
 * @author 章多亮
 * @since 2020/5/28 10:35
 */
class CachedParameterClassMeta extends AbstractClassMeta implements ParameterClassMeta {
    private final Parameter parameter;

    private CachedParameterClassMeta(Parameter parameter, Class<?> parameterType) {
        super(parameterType == null ? parameter.getType() : parameterType);
        this.parameter = parameter;
    }

    static ClassMeta createIfAbsent(Parameter parameter, Class<?> parameterType) {
        return CLASS_METAS.computeIfAbsent(parameterType, t -> create(parameter, t));
    }

    private static ClassMeta create(Parameter parameter, Class<?> parameterType) {
        if (!parameter.getType().isAssignableFrom(parameterType)) {
            throw new EbatisException("形参类型和实参类型不兼容");
        }
        return new CachedParameterClassMeta(parameter, parameterType);
    }

    @Override
    public Parameter getParameter() {
        return parameter;
    }
}

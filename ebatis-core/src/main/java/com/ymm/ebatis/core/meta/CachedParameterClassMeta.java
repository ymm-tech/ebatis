package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.exception.EbatisException;
import com.ymm.ebatis.core.generic.GenericType;

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
        GenericType type = GenericType.forParameter(parameter);
        Class<?> clazz;
        if (type.isCollection() || type.isArray()) {
            clazz = type.resolveGeneric(0);
        } else {
            clazz = type.resolve();
        }
        if (!clazz.isAssignableFrom(parameterType)) {
            throw new EbatisException("形参类型和实参类型不兼容");
        }
        return new CachedParameterClassMeta(parameter, parameterType);
    }

    @Override
    public Parameter getParameter() {
        return parameter;
    }
}

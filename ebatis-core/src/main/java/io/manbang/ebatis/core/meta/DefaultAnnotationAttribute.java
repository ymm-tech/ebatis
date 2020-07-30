package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.common.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 章多亮
 * @since 2020/6/3 13:58
 */
class DefaultAnnotationAttribute implements AnnotationAttribute {
    private final Method method;
    private final Class<?> attributeType;
    private final boolean annotation;
    private final boolean enumeration;
    private final boolean array;


    DefaultAnnotationAttribute(Method method) {
        this.method = method;

        Class<?> returnType = method.getReturnType();
        this.array = returnType.isArray();
        this.attributeType = array ? returnType.getComponentType() : returnType;

        this.annotation = attributeType.isAnnotation();
        this.enumeration = attributeType.isEnum();
    }


    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean isArray() {
        return array;
    }

    @Override
    public boolean isAnnotation() {
        return annotation;
    }

    @Override
    public boolean isEnum() {
        return enumeration;
    }

    @Override
    public Class<?> getAttributeType() {
        return attributeType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> A getFirstValue(Annotation instance) {
        Object value = getValue(instance);
        if (array) {
            Object[] values = (Object[]) value;

            if (values != null && values.length > 0) {
                return (A) values[0];
            } else {
                return null;
            }
        } else {
            return (A) value;
        }
    }

    @Override
    public <A> A getValue(Annotation instance) {
        return MethodUtils.invoke(method, instance);
    }
}

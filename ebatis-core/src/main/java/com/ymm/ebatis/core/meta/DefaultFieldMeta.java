package com.ymm.ebatis.core.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.exception.ReadMethodNotFoundException;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/21 18:27
 */
@ToString
class DefaultFieldMeta implements FieldMeta {
    private final Field field;
    private final Method readMethod;
    private final String name;

    DefaultFieldMeta(Field field) {
        this.field = field;
        this.readMethod = getReadMethod(field);
        this.name = getName(field);
    }

    private String getName(Field element) {
        return fromAnnotation(element)
                .orElseGet(field::getName);
    }

    private Optional<String> fromAnnotation(Field element) {
        Optional<String> nameOptional = fromFieldAnnotation(element);
        if (nameOptional.isPresent()) {
            return nameOptional;
        }

        return fromJsonPropertyAnnotation(element);
    }

    private Optional<String> fromJsonPropertyAnnotation(Field element) {
        // 支持JSON的Field映射
        JsonProperty jsonField = element.getAnnotation(JsonProperty.class);
        if (jsonField != null) {
            String n = jsonField.value();
            if (StringUtils.isNotBlank(n)) {
                return Optional.of(n);
            }
        }
        return Optional.empty();
    }

    private Optional<String> fromFieldAnnotation(Field element) {
        com.ymm.ebatis.core.annotation.Field annotation = element.getAnnotation(com.ymm.ebatis.core.annotation.Field.class);
        if (annotation != null) {
            String n = annotation.value();
            if (StringUtils.isNotBlank(n)) {
                return Optional.of(n);
            }

            n = annotation.name();
            if (StringUtils.isNotBlank(n)) {
                return Optional.of(n);
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    private Method getReadMethod(Field field) {
        BeanInfo beanInfo = Introspector.getBeanInfo(field.getDeclaringClass());
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (propertyDescriptor.getName().equals(field.getName())) {
                return propertyDescriptor.getReadMethod();
            }
        }

        // 字段必须要有读方法
        throw new ReadMethodNotFoundException(field.toString());
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Method getReadMethod() {
        return readMethod;
    }

    @Override
    public String getName() {
        return name;
    }
}

package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Ignore;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Modifier;

/**
 * @author 章多亮
 * @since 2020/5/21 19:18
 */
class DefaultParameterClassMeta implements ParameterClassMeta {
    private final Class<?> clazz;
    private final FieldMeta[] fieldMetas;

    DefaultParameterClassMeta(Class<?> clazz) {
        this.clazz = clazz;
        this.fieldMetas = getFieldMetas(clazz);
    }

    @SneakyThrows
    private FieldMeta[] getFieldMetas(Class<?> clazz) {
        // 非静态的私有字段才符合条件
        return FieldUtils.getAllFieldsList(clazz)
                .stream()
                // 必须是私有字段
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                // 非静态字段
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                // 非临时字段
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                // 没有加上 Ignore注解
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .map(FieldMeta::from)
                .toArray(FieldMeta[]::new);
    }

    @Override
    public FieldMeta[] getFields() {
        return fieldMetas;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }
}

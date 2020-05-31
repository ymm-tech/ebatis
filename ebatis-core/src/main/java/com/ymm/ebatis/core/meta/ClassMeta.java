package com.ymm.ebatis.core.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/26 10:12
 */
public interface ClassMeta extends AnnotatedMeta<Class<?>> {

    static ClassMeta parameter(Parameter parameter, Class<?> parameterType) {
        return CachedParameterClassMeta.createIfAbsent(parameter, parameterType);
    }

    static ClassMeta field(Field field, Class<?> fieldType) {
        return CachedFieldClassMeta.createIfAbsent(field, fieldType);
    }

    static ClassMeta of(Class<?> clazz) {
        return CachedClassMeta.createIfAbsent(clazz);
    }

    /**
     * 获取方法元数据列表
     *
     * @return 方法元数据列表
     */
    List<MethodMeta> getMethodMetas();

    /**
     * 根据指定方法找到对应的方法元数据，可能为空
     *
     * @param method 方法
     * @return 方法元数据
     */
    Optional<MethodMeta> findMethodMeta(Method method);

    /**
     * 根据指定方法找到对应的方法元数据
     *
     * @param method 方法
     * @return 方法元数据
     */
    MethodMeta getMethodMeta(Method method);

    /**
     * 获取类的字段列表元数据
     *
     * @return 字段元数据列表
     */
    List<FieldMeta> getFieldMetas();

    /**
     * 根据指定字段找到定的字段元数据，可以为空
     *
     * @param field 字段
     * @return 字段元数据
     */
    Optional<FieldMeta> findFieldMeta(Field field);

    /**
     * 根据指定字段找到定的字段元数据
     *
     * @param field 字段
     * @return 字段元数据
     */
    FieldMeta getFieldMeta(Field field);

    Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses();
}

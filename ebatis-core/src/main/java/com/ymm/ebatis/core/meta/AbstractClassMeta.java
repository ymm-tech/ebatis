package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.exception.FieldMetaNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 章多亮
 * @since 2020/5/27 17:44
 */
public abstract class AbstractClassMeta implements ClassMeta {
    protected static final Map<Class<?>, ClassMeta> CLASS_METAS = new ConcurrentHashMap<>();
    private final Class<?> clazz;
    private final Map<Field, FieldMeta> fieldMetas;
    private final Map<Class<? extends Annotation>, List<FieldMeta>> queryClauses;


    protected AbstractClassMeta(Class<?> clazz) {
        this.clazz = clazz;
        this.fieldMetas = getFieldMetas(clazz);
        this.queryClauses = getQueryClauses(fieldMetas);
    }

    private Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(Map<Field, FieldMeta> fieldMetas) {
        Map<Class<? extends Annotation>, List<FieldMeta>> metas = fieldMetas.values()
                .stream()
                .collect(Collectors.groupingBy(FieldMeta::getQueryClauseAnnotationClass, Collectors.toList()));
        return Collections.unmodifiableMap(metas);
    }

    private Map<Field, FieldMeta> getFieldMetas(Class<?> clazz) {
        Map<Field, FieldMeta> metas = Arrays.stream(clazz.getDeclaredFields())
                .filter(this::filterField)
                .map(FieldMeta::of)
                .collect(Collectors.toMap(FieldMeta::getElement, meta -> meta));

        return Collections.unmodifiableMap(metas);
    }

    protected boolean filterField(Field field) {
        return QueryClauses.filterField(field);
    }

    @Override
    public List<MethodMeta> getMethodMetas() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<MethodMeta> findMethodMeta(Method method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MethodMeta getMethodMeta(Method method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FieldMeta> getFieldMetas() {
        return Collections.emptyList();
    }

    @Override
    public Optional<FieldMeta> findFieldMeta(Field field) {
        return Optional.ofNullable(fieldMetas.get(field));
    }

    @Override
    public FieldMeta getFieldMeta(Field field) {
        return fieldMetas.computeIfAbsent(field, f -> {
            throw new FieldMetaNotFoundException(field.toString());
        });
    }

    @Override
    public Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses() {
        return queryClauses;
    }

    @Override
    public Class<?> getElement() {
        return clazz;
    }
}

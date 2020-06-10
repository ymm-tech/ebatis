package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.exception.FieldMetaNotFoundException;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private final Map<Field, FieldMeta> fieldMetaMap;
    private final Map<Class<? extends Annotation>, List<FieldMeta>> queryClauses;
    private final List<FieldMeta> fieldMetas;
    private final List<MethodMeta> methodMetas;


    protected AbstractClassMeta(Class<?> clazz) {
        this.clazz = clazz;
        this.fieldMetas = getFieldMetas(clazz);
        this.fieldMetaMap = getFieldMetaMap(fieldMetas);
        this.queryClauses = getQueryClauses(fieldMetas);

        this.methodMetas = Collections.emptyList();
    }

    private List<FieldMeta> getFieldMetas(Class<?> clazz) {
        List<FieldMeta> metas = FieldUtils.getAllFieldsList(clazz)
                .stream()
                .filter(this::filterField)
                .map(FieldMeta::of)
                .collect(Collectors.toList());

        return Collections.unmodifiableList(metas);
    }

    private Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(List<FieldMeta> fieldMetas) {
        Map<Class<? extends Annotation>, List<FieldMeta>> metas = fieldMetas.stream()
                .collect(Collectors.groupingBy(FieldMeta::getQueryClauseAnnotationClass, Collectors.toList()));

        return Collections.unmodifiableMap(metas);
    }

    private Map<Field, FieldMeta> getFieldMetaMap(List<FieldMeta> fieldMetas) {
        Map<Field, FieldMeta> metas = fieldMetas.stream()
                .collect(Collectors.toMap(FieldMeta::getElement, meta -> meta));

        return Collections.unmodifiableMap(metas);
    }

    protected boolean filterField(Field field) {
        return QueryClauses.filterField(field);
    }

    @Override
    public List<MethodMeta> getMethodMetas() {
        return methodMetas;
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
        return fieldMetas;
    }

    @Override
    public Optional<FieldMeta> findFieldMeta(Field field) {
        return Optional.ofNullable(fieldMetaMap.get(field));
    }

    @Override
    public FieldMeta getFieldMeta(Field field) {
        return fieldMetaMap.computeIfAbsent(field, f -> {
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

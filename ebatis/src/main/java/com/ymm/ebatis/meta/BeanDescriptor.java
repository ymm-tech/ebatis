package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Ignore;
import com.ymm.ebatis.annotation.Version;
import com.ymm.ebatis.common.DslUtils;
import com.ymm.ebatis.common.Lazy;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 章多亮
 */
public class BeanDescriptor {
    private static final Map<Class<?>, List<FieldConditionMeta>> CONDITION_CLASS_FIELDS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, BeanDescriptor> CONDITIONS = new ConcurrentHashMap<>();
    private final Class<?> beanClass;
    private final List<FieldConditionMeta> fieldElements;
    private final List<FieldConditionMeta> fieldElementsWithoutIgnore;
    private final Map<Class<? extends Annotation>, List<FieldConditionMeta>> fieldElementGroup;
    private final Lazy<Optional<FieldConditionMeta>> version;
    private final Lazy<Optional<FieldConditionMeta>> id;
    private final Lazy<Optional<FieldConditionMeta>> missing;

    private BeanDescriptor(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.fieldElements = Collections.unmodifiableList(CONDITION_CLASS_FIELDS.computeIfAbsent(beanClass, this::getFields));
        this.fieldElementsWithoutIgnore = Collections.unmodifiableList(fieldElements.stream().filter(field -> !field.isAnnotationPresent(Ignore.class)).collect(Collectors.toList()));
        this.fieldElementGroup = Collections.unmodifiableMap(this.fieldElementsWithoutIgnore.stream().collect(Collectors.groupingBy(DslUtils::groupByQueryClauseType)));
        this.version = new Lazy<>(() -> fieldElements.stream().filter(ConditionMeta::isVersion).findFirst());
        this.id = new Lazy<>(() -> fieldElements.stream().filter(ConditionMeta::isId).findFirst());
        this.missing = new Lazy<>(() -> fieldElements.stream().filter(ConditionMeta::isMissing).findFirst());
    }


    public static BeanDescriptor of(Object bean) {
        Objects.requireNonNull(bean, "搜索条件不能为空");
        return CONDITIONS.computeIfAbsent(bean.getClass(), BeanDescriptor::new);
    }

    public static BeanDescriptor of(Class<?> beanClass) {
        return CONDITIONS.computeIfAbsent(beanClass, BeanDescriptor::new);
    }

    public List<FieldConditionMeta> getFieldElementsWithoutIgnore() {
        return fieldElementsWithoutIgnore;
    }

    private List<FieldConditionMeta> getFields(Class<?> beanClass) {
        return FieldUtils.getAllFieldsList(beanClass).stream()
                .filter(field -> !field.isAnnotationPresent(Version.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .map(FieldConditionMeta::new)
                .collect(Collectors.toList());
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Optional<FieldConditionMeta> getVersion() {
        return version.get();
    }

    public Optional<FieldConditionMeta> getId() {
        return id.get();
    }

    public Optional<FieldConditionMeta> getMissing() {
        return missing.get();
    }

    /**
     * 返回所有的条件列表，包含 {@link Ignore}注解的字段
     *
     * @return 条件列表
     */
    public List<FieldConditionMeta> getFieldElements() {
        return fieldElements;
    }

    public Map<Class<? extends Annotation>, List<FieldConditionMeta>> getFieldElementGroup() {
        return fieldElementGroup;
    }
}

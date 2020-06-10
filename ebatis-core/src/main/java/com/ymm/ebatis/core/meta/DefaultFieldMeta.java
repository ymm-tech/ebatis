package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Prefix;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.builder.QueryBuilderFactory;
import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.exception.ReadMethodInvokeException;
import com.ymm.ebatis.core.exception.ReadMethodNotFoundException;
import com.ymm.ebatis.core.generic.GenericType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/27 19:16
 */
class DefaultFieldMeta extends AbstractConditionMeta<Field> implements FieldMeta {
    private final Field field;

    private final boolean basic;
    private final boolean basicArrayOrCollection;
    private final Method readMethod;
    private final Class<? extends Annotation> queryClauseAnnotationClass;
    private final Annotation queryClauseAnnotation;
    private final QueryBuilderFactory queryBuilderFactory;

    DefaultFieldMeta(Field field) {
        super(field, field.getType());
        this.field = field;
        this.readMethod = getReadMethod(field);

        Class<?> type = getActualType(field);
        this.basic = MetaUtils.isBasic(type);
        this.basicArrayOrCollection = isArrayOrCollection() && basic;

        this.queryClauseAnnotationClass = QueryClauses.getQueryClauseClass(this);

        Optional<? extends Annotation> annotation = findAnnotation(queryClauseAnnotationClass);
        this.queryClauseAnnotation = annotation.orElse(null);

        this.queryBuilderFactory = annotation.flatMap(a -> AnnotationUtils.findAttribute(a, QueryType.class)).orElse(QueryType.AUTO).getQueryBuilderFactory();
    }

    private Class<?> getActualType(Field field) {
        Class<?> type;
        if (isArray()) {
            type = getType().getComponentType();
        } else if (isCollection()) {
            type = GenericType.forField(field).resolveGeneric(0);
        } else {
            type = getType();
        }
        return type;
    }

    @Override
    public Field getElement() {
        return field;
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
    protected String getName(Field field) {
        String name = super.getName(field);
        name = StringUtils.isBlank(name) ? field.getName() : name;

        Class<?> declaringClass = field.getDeclaringClass();
        String prefix = getPrefix(declaringClass);

        if (prefix == null) {
            return name;
        } else {
            return String.format("%s.%s", prefix, name);
        }
    }

    /**
     * 获取字段前缀
     *
     * @param clazz 字段所在类
     * @return 前缀
     */
    private String getPrefix(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Prefix.class)) {
            return StringUtils.trimToNull(clazz.getAnnotation(Prefix.class).value());
        } else {
            return null;
        }
    }

    @Override
    public <A extends Annotation> Optional<A> findAttributeAnnotation(Class<A> annotationClass) {
        return getQueryClauseAnnotation().flatMap(a -> AnnotationUtils.findAttributeAnnotation(a, annotationClass));
    }

    @Override
    public Object getValue(Object instance) {
        try {
            return readMethod.invoke(instance);
        } catch (Exception e) {
            throw new ReadMethodInvokeException(e);
        }
    }

    @Override
    public boolean isBasic() {
        return basic;
    }

    @Override
    public boolean isBasicArrayOrCollection() {
        return basicArrayOrCollection;
    }

    @Override
    public Class<? extends Annotation> getQueryClauseAnnotationClass() {
        return queryClauseAnnotationClass;
    }

    @Override
    public QueryBuilderFactory getQueryBuilderFactory() {
        return queryBuilderFactory;
    }

    @Override
    public Optional<Annotation> getQueryClauseAnnotation() {
        return Optional.ofNullable(queryClauseAnnotation);
    }


    @Override
    public Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(Object instance) {
        return ClassMeta.field(field, instance == null ? null : instance.getClass()).getQueryClauses();
    }
}

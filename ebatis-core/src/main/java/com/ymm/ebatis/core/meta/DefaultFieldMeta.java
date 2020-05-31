package com.ymm.ebatis.core.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.builder.QueryBuilderFactory;
import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.exception.ReadMethodInvokeException;
import com.ymm.ebatis.core.exception.ReadMethodNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;

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
public class DefaultFieldMeta extends AbstractConditionMeta implements FieldMeta {
    private final Field field;

    private final boolean basic;
    private final boolean basicArrayOrCollection;
    private final String name;
    private final Method readMethod;
    private final Class<? extends Annotation> queryClauseAnnotationClass;
    private final Annotation queryClauseAnnotation;
    private final QueryBuilderFactory queryBuilderFactory;

    public DefaultFieldMeta(Field field) {
        super(field.getType());
        this.field = field;
        this.readMethod = getReadMethod(field);

        Class<?> type = getActualType(field);
        this.basic = MetaUtils.isBasic(type);
        this.basicArrayOrCollection = isArrayOrCollection() && basic;
        this.name = getName(field);

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
            type = ResolvableType.forField(field).resolveGeneric(0);
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

    private String getName(Field field) {
        String name; // NOSONAR

        com.ymm.ebatis.core.annotation.Field fieldAnnotation = field.getAnnotation(com.ymm.ebatis.core.annotation.Field.class);
        if (fieldAnnotation != null) {
            name = fieldAnnotation.name();
            if (StringUtils.isNotBlank(name)) {
                return name;
            }

            name = fieldAnnotation.value();
            if (StringUtils.isNotBlank(name)) {
                return name;
            }
        }

        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            name = jsonProperty.value();
            if (StringUtils.isNotBlank(name)) {
                return name;
            }
        }

        return field.getName();

    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public <A extends Annotation> Optional<A> findAttributeAnnotation(Class<A> annotationClass) {
        return getQueryClauseAnnotation().flatMap(a -> AnnotationUtils.findAttribute(a, annotationClass));
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

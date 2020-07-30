package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.annotation.Prefix;
import io.manbang.ebatis.core.annotation.QueryType;
import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import io.manbang.ebatis.core.common.AnnotationUtils;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.exception.EbatisException;
import io.manbang.ebatis.core.exception.ReadMethodInvokeException;
import io.manbang.ebatis.core.exception.ReadMethodNotFoundException;
import io.manbang.ebatis.core.generic.GenericType;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
    private final boolean termsQuery;
    private final Map<Class<? extends Annotation>, Optional<? extends Annotation>> metas = new ConcurrentHashMap<>();

    DefaultFieldMeta(Field field) {
        super(field, field.getType(), field.getGenericType());
        this.field = field;
        this.readMethod = getReadMethod(field);

        Class<?> type = getActualType(field);
        this.basic = MetaUtils.isBasic(type);
        this.basicArrayOrCollection = isArrayOrCollection() && basic;

        this.queryClauseAnnotationClass = QueryClauses.getQueryClauseClass(this);

        Optional<? extends Annotation> annotation = findAnnotation(queryClauseAnnotationClass);
        this.queryClauseAnnotation = annotation.orElse(null);

        this.queryBuilderFactory = annotation.flatMap(a -> AnnotationUtils.findAttribute(a, QueryType.class)).orElse(QueryType.AUTO).getQueryBuilderFactory();

        this.termsQuery = queryBuilderFactory == QueryBuilderFactory.terms();
        validate();
    }

    private void validate() {
        boolean isExists = queryBuilderFactory == QueryBuilderFactory.exists();
        boolean isBoolean = Boolean.TYPE == field.getType() || Boolean.class == field.getType();
        if (isExists && !isBoolean) {
            throw new ConditionNotSupportException(field + ":Exists query must be boolean or Boolean!");
        }
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

    private Method getReadMethod(Field field) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(field.getDeclaringClass());
        } catch (IntrospectionException e) {
            throw new EbatisException("Introspect exception!", e);
        }
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
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<A> findAttributeAnnotation(Class<A> annotationClass) {
        return (Optional<A>) metas.computeIfAbsent(annotationClass, clazz ->
                getQueryClauseAnnotation().flatMap(a -> AnnotationUtils.findAttributeAnnotation(a, clazz)));
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

    @Override
    public boolean isTermsQuery() {
        return termsQuery;
    }
}

package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Ignore;
import com.ymm.ebatis.core.annotation.QueryType;
import com.ymm.ebatis.core.builder.QueryClauseType;
import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.common.DslUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author duoliang.zhang
 */
public class FieldConditionMeta extends AbstractConditionMeta<Field> {
    private final Method readMethod;
    private final List<FieldConditionMeta> children;
    private final Map<Class<? extends Annotation>, List<FieldConditionMeta>> childrenGroup;
    private final Boolean isExistsField;

    public FieldConditionMeta(ConditionMeta<?> parent, Field field) {
        super(parent, field);
        this.readMethod = DslUtils.getReadMethod(field);
        this.children = getChildrenIfNecessary(field);
        this.childrenGroup = this.children.stream().collect(Collectors.groupingBy(DslUtils::groupByQueryClauseType));
        this.isExistsField = getQueryClauseAnnotation().flatMap(annotation -> AnnotationUtils.findAttribute(annotation, QueryType.class))
                .map(QueryType.EXISTS::equals).orElse(Boolean.FALSE);

    }

    public FieldConditionMeta(Field field) {
        this(null, field);
    }

    @Override
    protected Annotation getQueryClauseAnnotation(AnnotatedElement element) {
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            QueryClauseType clauseType = QueryClauseType.valueOf(annotation.annotationType());
            if (clauseType != null) {
                return annotation;
            }
        }
        return null;
    }

    private List<FieldConditionMeta> getChildrenIfNecessary(Field field) {
        // 如果是组合条件，继续分解
        if (isCompound()) {
            Class<?> basicClass;
            if (Collection.class.isAssignableFrom(field.getType())) {
                basicClass = ResolvableType.forType(field.getGenericType()).resolveGeneric(0);
            } else if (field.getType().isArray()) {
                basicClass = field.getType().getComponentType();
            } else {
                basicClass = field.getType();
            }
            return FieldUtils.getAllFieldsList(basicClass).stream()
                    // 忽略带@Ignore注解的条件
                    .filter(f -> !f.isAnnotationPresent(Ignore.class))
                    // 非静态字段
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    // 私有字段
                    .filter(f -> Modifier.isPrivate(f.getModifiers()))
                    .map(f -> new FieldConditionMeta(this, f))
                    .collect(Collectors.toList());

        } else {
            return Collections.emptyList();
        }
    }

    public Map<Class<? extends Annotation>, List<FieldConditionMeta>> getChildrenGroup() {
        return childrenGroup;
    }

    public List<FieldConditionMeta> getChildren() {
        return children;
    }

    public Boolean isExistsField() {
        return isExistsField;
    }

    @Override
    protected String getName(Field element) {
        if (isParentNested()) {
            return String.format("%s.%s", getParent().getName(), super.getName(element));
        }
        return super.getName(element);
    }

    @Override
    protected String getNameFromElement(Field element) {
        return element.getName();
    }

    @Override
    protected Class<?> getConditionType(Field element) {
        return element.getType();
    }


    @Override
    public <A extends Annotation> Optional<A> getParentAnnotation(Class<A> aClass) {
        return getAnnotation(aClass);
    }


    @Override
    public <T> T getValue(Object instance) {
        return (T) DslUtils.getFieldValue(readMethod, instance);
    }


    @Override
    protected boolean getArrayOrCollectionBasicCondition(Field element) {
        Class<?> parameterType = element.getType();
        if (Collection.class.isAssignableFrom(parameterType)) {
            return DslUtils.isBasicClass(ResolvableType.forType(element.getGenericType()).resolveGeneric(0));
        } else if (parameterType.isArray()) {
            return DslUtils.isBasicClass(parameterType.getComponentType());
        } else {
            return false;
        }
    }
}

package com.ymm.ebatis.core.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.Range;
import com.ymm.ebatis.core.domain.Script;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * 查询条件
 *
 * @author 章多亮
 */
@ToString(of = {"name", "conditionType"})
public abstract class AbstractConditionMeta<T extends AnnotatedElement> implements ConditionMeta<T> {
    /**
     * 查询条件属性字段
     */
    private final T element;
    /**
     * 字段类型是否是基本类型
     */
    private final boolean basicCondition;
    /**
     * 是否忽略空指针条件
     */
    private final boolean ignoreNull;
    /**
     * 字段映射名称，与ES字段名称相同
     */
    private final String name;
    private final boolean arrayOrCollectionCondition;
    private final Annotation queryClauseAnnotation;
    private final boolean nestedQueryClause;
    private final ConditionMeta<?> parent;
    private final Class<?> conditionType;
    private final boolean rangeCondition;
    private final boolean scriptCondition;
    private final boolean parentNested;
    private final boolean compoundCondition;
    private final boolean arrayOrCollectionBasicCondition;

    public AbstractConditionMeta(ConditionMeta<?> parent, T element) {
        this.parent = parent;
        this.element = element;
        this.conditionType = getConditionType(element);

        // 不需要分解的对象
        this.basicCondition = DslUtils.isBasicClass(conditionType);
        this.arrayOrCollectionCondition = DslUtils.isArrayOrCollection(conditionType);
        this.rangeCondition = Range.class.isAssignableFrom(conditionType);
        this.scriptCondition = Script.class.isAssignableFrom(conditionType);
        this.arrayOrCollectionBasicCondition = getArrayOrCollectionBasicCondition(element);

        this.compoundCondition = !(basicCondition || (arrayOrCollectionCondition && arrayOrCollectionBasicCondition) || rangeCondition || scriptCondition);

        this.parentNested = parent != null && parent.isNestedQueryClause();
        Field f = element.getAnnotation(Field.class);
        this.ignoreNull = f == null || f.ignoreNull();
        this.queryClauseAnnotation = getQueryClauseAnnotation(element);
        this.nestedQueryClause = DslUtils.isNestedQueryClause(queryClauseAnnotation);
        this.name = getName(element);
    }

    public AbstractConditionMeta(T element) {
        this(null, element);
    }

    @Override
    public ConditionMeta<?> getParent() {
        return parent;
    }

    @Override
    public <A extends Annotation> Optional<A> getAttributeAnnotation(Class<A> aClass) {
        return getQueryClauseAnnotation().flatMap(annotation -> AnnotationUtils.findAttribute(annotation, aClass));
    }

    public boolean isParentNested() {
        return parentNested;
    }

    @Override
    public boolean isNestedQueryClause() {
        return nestedQueryClause;
    }

    @Override
    public Class<? extends Annotation> getQueryClauseClass() {
        return queryClauseAnnotation == null ? Must.class : queryClauseAnnotation.annotationType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Optional<A> getQueryClauseAnnotation() {
        return Optional.ofNullable((A) queryClauseAnnotation);
    }

    /**
     * 获取指定条件条件的查询注解
     *
     * @param element 条件条件
     * @return 注解
     */
    protected abstract Annotation getQueryClauseAnnotation(AnnotatedElement element);

    protected String getName(T element) {
        Field f = element.getAnnotation(Field.class);
        if (f != null) {
            String n = f.value();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }

            n = f.name();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }
        }

        // 支持JSON的Field映射
        JsonProperty jsonField = element.getAnnotation(JsonProperty.class);
        if (jsonField != null) {
            String n = jsonField.value();
            if (StringUtils.isNotBlank(n)) {
                return n;
            }
        }

        return getNameFromElement(element);
    }

    /**
     * 获取查询属性名称
     *
     * @param element 查询条件对象
     * @return 属性名
     */
    protected abstract String getNameFromElement(T element);

    /**
     * 获取查询条件对象类型
     *
     * @param element 查询条件对象
     * @return 查询条件实际的类型
     */
    protected abstract Class<?> getConditionType(T element);

    @Override
    public T getElement() {
        return element;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBasic() {
        return basicCondition;
    }

    @Override
    public boolean isIgnoreNull() {
        return ignoreNull;
    }


    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return element.isAnnotationPresent(annotationClass);
    }

    @Override
    public boolean isArrayOrCollection() {
        return arrayOrCollectionCondition;
    }

    @Override
    public boolean isRange() {
        return rangeCondition;
    }

    @Override
    public boolean isScript() {
        return scriptCondition;
    }

    @Override
    public Class<?> getElementType() {
        return conditionType;
    }

    @Override
    public boolean isCompound() {
        return compoundCondition;
    }

    @Override
    public boolean isArrayOrCollectionBasicCondition() {
        return arrayOrCollectionBasicCondition;
    }

    /**
     * 判断集合或数组元素是否为基本类型
     *
     * @param element 查询条件对象
     * @return 集合元素是否是基本类型
     */
    protected abstract boolean getArrayOrCollectionBasicCondition(T element);
}


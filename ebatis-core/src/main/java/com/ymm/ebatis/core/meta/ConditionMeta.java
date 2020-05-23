package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Id;
import com.ymm.ebatis.core.annotation.Missing;
import com.ymm.ebatis.core.annotation.Must;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.annotation.Version;
import com.ymm.ebatis.core.exception.AnnotationNotPresentException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2019/12/18 18:04
 */
public interface ConditionMeta<T extends AnnotatedElement> {
    /**
     * 获取查询条件本身
     *
     * @return 查询条件实例
     */
    T getElement();

    /**
     * 获取条件上的注解数量
     *
     * @return 数量
     */
    default int getAnnotationCount() {
        return getElement().getAnnotations().length;
    }

    /**
     * 判断条件上是否有注解
     *
     * @return 如果条件上有任何注解，返回<code>true</code>
     */
    default boolean hasAnyAnnotation() {
        return getAnnotationCount() > 0;
    }

    /**
     * 获取条件上的所有注解
     *
     * @return 注解列表
     */
    default Annotation[] getAnnotations() {
        return getElement().getAnnotations();
    }

    /**
     * 判断条件是否是脚本
     *
     * @return 如果是脚本，返回<code>true</code>
     */
    boolean isScript();

    /**
     * 获取条件类型
     *
     * @return 条件类型
     */
    Class<?> getElementType();

    /**
     * 获取条件上的指定类型注解
     *
     * @param aClass 注解类型
     * @param <A>    注解类型泛型
     * @return 注解实例
     */
    default <A extends Annotation> Optional<A> getAnnotation(Class<A> aClass) {
        return Optional.ofNullable(getElement().getAnnotation(aClass));
    }

    /**
     * 获取条件上的指定类型注解
     *
     * @param aClass 注解类型
     * @param <A>    注解类型泛型
     * @return 注解实例
     */
    default <A extends Annotation> A getAnnotationRequired(Class<A> aClass) {
        return getAnnotation(aClass).orElseThrow(() -> new AnnotationNotPresentException(aClass.getName()));
    }

    /**
     * 获取指定泛型类型注解
     *
     * @param aClass 注解类型
     * @param <A>    类型泛型类型
     * @return 注解
     */
    <A extends Annotation> Optional<A> getParentAnnotation(Class<A> aClass);

    /**
     * 获取指定泛型类型注解，如果不存在，抛出异常
     *
     * @param aClass 注解类型
     * @param <A>    类型泛型类型
     * @return 注解
     */
    default <A extends Annotation> A getParentAnnotationRequired(Class<A> aClass) {
        return getParentAnnotation(aClass).orElseThrow(() -> new AnnotationNotPresentException(aClass.getName()));
    }

    /**
     * 获取查询语句注解
     *
     * @param <A> 查询语句注解类型
     * @return 注解
     */
    <A extends Annotation> Optional<A> getQueryClauseAnnotation();

    /**
     * 获取属性名称
     *
     * @return 属性名称
     */
    String getName();

    /**
     * 判断查询条件是否是基本类型
     *
     * @return 如果是基本类型，返回<code>true</code>
     */
    boolean isBasic();

    /**
     * 判断当前查询条件是否空值忽略
     *
     * @return 如果空值忽略，返回<code>true</code>
     */
    boolean isIgnoreNull();

    /**
     * 判断当前条件是否是版本字段
     *
     * @return 如果是版本字段，返回<code>true</code>
     */
    default boolean isVersion() {
        return isAnnotationPresent(Version.class);
    }

    /**
     * 判断当前条件是否是Id字段
     *
     * @return 如果是Id字段，返回<code>true</code>
     */
    default boolean isId() {
        return isAnnotationPresent(Id.class);
    }

    /**
     * 判断当前条件是否是Missing字段
     *
     * @return 如果是Missing字段，返回<code>true</code>
     */
    default boolean isMissing() {
        return isAnnotationPresent(Missing.class);
    }

    /**
     * 获取查询条件对应的实际值
     *
     * @param instance 查询条件所属对象实例
     * @return 属性值
     */
    <V> V getValue(Object instance);

    /**
     * 判断指定注解类型是否存在此查询条件上
     *
     * @param annotationClass 注解类型
     * @return 如果注解类型存在，返回<code>true</code>
     */
    boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

    /**
     * 判断当前条件类型是否是数据或者集合
     *
     * @return 如果当前条件类型是数据或者集合，返回<code>true</code>
     */
    boolean isArrayOrCollection();

    /**
     * 判断当前条件类型是否是方位类型
     *
     * @return 如果当前条件是范围类型，返回<code>true</code>
     */
    boolean isRange();

    /**
     * 判断当前Element是否是嵌套语句
     *
     * @return 如果是嵌套语句，返回<code>true</code>
     */
    boolean isNestedQueryClause();

    /**
     * 获取查询语句的注解类型，如果没有注解，则为 {@link Must 必须语句}
     *
     * @return 查询语句注解类型
     */
    Class<? extends Annotation> getQueryClauseClass();


    /**
     * 获取父条件列表
     *
     * @return 父条件
     */
    default ConditionMeta<?> getParent() {
        return null;
    }


    /**
     * 获取嵌套注解
     *
     * @param aClass 嵌套注解类型
     * @param <A>    嵌套注解类型方形
     * @return 注解
     * @see com.ymm.ebatis.core.annotation.Must Must
     * @see com.ymm.ebatis.core.annotation.MustNot
     * @see com.ymm.ebatis.core.annotation.Should Should
     * @see com.ymm.ebatis.core.annotation.Filter Filter
     * @see Search Query
     */
    <A extends Annotation> Optional<A> getAttributeAnnotation(Class<A> aClass);

    /**
     * 判断当前对象是否组合条件
     *
     * @return 如果当前条件是对象组合条件，返回<code>true</code>
     */
    boolean isCompound();

    /**
     * 判断当前对象数组或集合时候，元素是否是基本类型
     * @return 如果当前是集合或数组，且集合和数组类型为基本类型，返回<code>true</code>
     */
    boolean isArrayOrCollectionBasicCondition();
}

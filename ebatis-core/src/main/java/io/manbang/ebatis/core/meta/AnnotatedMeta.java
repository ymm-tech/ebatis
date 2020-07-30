package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.exception.AnnotationNotPresentException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/27 17:08
 */
public interface AnnotatedMeta<E extends AnnotatedElement> {

    /**
     * 获取被注解的元素
     *
     * @return 被注解的元素
     */
    E getElement();

    /**
     * 判断指定类型注解是否在注解元素上存在
     *
     * @param annotationClass 注解类型
     * @return 指定类型注解存在的话，返回<code>true</code>
     */
    default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return getElement().isAnnotationPresent(annotationClass);
    }

    /**
     * 获取指定类型注解
     *
     * @param annotationClass 注解类型
     * @param <A>             注解类型泛型
     * @return 注解实例
     */
    default <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationClass) {
        return Optional.ofNullable(getElement().getAnnotation(annotationClass));
    }

    /**
     * 获取注解元素上的所有注解
     *
     * @return 注解列表
     */
    default Annotation[] getAnnotations() {
        return getElement().getAnnotations();
    }

    /**
     * 获取方法上的指定类型注解，必须存在，否则异常
     *
     * @param annotationClass 注解类型
     * @param <A>             具体注解类型
     * @return 注解的实例
     */
    default <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return findAnnotation(annotationClass).orElseThrow(() -> new AnnotationNotPresentException(annotationClass.getName()));
    }
}

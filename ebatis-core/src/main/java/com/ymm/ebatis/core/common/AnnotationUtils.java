package com.ymm.ebatis.core.common;

import com.ymm.ebatis.core.meta.AnnotationAttribute;
import com.ymm.ebatis.core.meta.AnnotationMeta;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/1/8 10:06
 */
public class AnnotationUtils {
    private AnnotationUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取指定注解，指定属性类型的注解，如果属性注解是数组，则返回的第零个元素，否则直接返回对应属性注解，可能指定的注解不存在对应类型的属性注解，则返回空
     *
     * @param annotation     注解实例
     * @param attributeClass 属性类型
     * @param <A>            属性注解类型泛型
     * @return 属性注解实例
     */
    public static <A> Optional<A> findAttributeAnnotation(Annotation annotation, Class<A> attributeClass) {
        if (annotation == null || attributeClass == null) {
            return Optional.empty();
        }

        return AnnotationMeta.of(annotation)
                .getAttributes()
                .stream()
                .filter(AnnotationAttribute::isAnnotation)
                .filter(a -> attributeClass == a.getAttributeType())
                .findFirst().map(a -> a.getFirstValue(annotation));
    }

    /**
     * 获取指定注解，指定属性类型的属性，如果属性注解是数组，则返回的第零个元素，否则直接返回对应属性注解，可能指定的注解不存在对应类型的属性注解，则返回空
     *
     * @param annotation     注解实例
     * @param attributeClass 属性类型
     * @param <A>            属性注解类型泛型
     * @return 属性注解实例
     */
    public static <A> Optional<A> findFirstAttribute(Annotation annotation, Class<A> attributeClass) {
        if (annotation == null || attributeClass == null) {
            return Optional.empty();
        }

        return AnnotationMeta.of(annotation)
                .getAttributes()
                .stream()
                .filter(a -> attributeClass == a.getAttributeType())
                .findFirst().map(a -> a.getFirstValue(annotation));
    }

    /**
     * 获取指定注解，指定属名的属性值
     *
     * @param annotation    注解实例
     * @param attributeName 属性名
     * @param <A>           属性注解类型泛型
     * @return 属性注解实例
     */
    public static <A> Optional<A> findAttribute(Annotation annotation, String attributeName) {
        if (annotation == null || attributeName == null) {
            return Optional.empty();
        }

        return AnnotationMeta.of(annotation)
                .getAttributes()
                .stream()
                .filter(a -> Objects.equals(attributeName, a.getName()))
                .findFirst().map(a -> a.getValue(annotation));
    }

    /**
     * 获取指定注解，指定属性类型的值
     *
     * @param annotation    注解实例
     * @param attributeType 属性类型
     * @param <A>           属性泛型
     * @return 属性注解实例
     */
    public static <A> Optional<A> findAttribute(Annotation annotation, Class<A> attributeType) {
        if (annotation == null || attributeType == null) {
            return Optional.empty();
        }

        return AnnotationMeta.of(annotation)
                .getAttributes()
                .stream()
                .filter(a -> attributeType == a.getAttributeType())
                .findFirst().map(a -> a.getValue(annotation));
    }
}

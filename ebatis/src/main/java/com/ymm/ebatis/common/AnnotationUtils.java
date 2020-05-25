package com.ymm.ebatis.common;

import com.ymm.ebatis.exception.AnnotationNotPresentException;
import lombok.Synchronized;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/8 10:06
 */
public class AnnotationUtils {
    private static final Map<Class<? extends Annotation>, Map<Class<?>, Method>> ANNOTATION_METHODS = new HashMap<>();

    private AnnotationUtils() {
    }

    /**
     * 获取指定注解属性注解数组提供者的第一个注解
     *
     * @param attrAnnotations 数组类型属性注解提供方
     * @param <A>             属性注解类型泛型
     * @return 属性注解
     */
    public static <A> Optional<A> getAnnotation(Supplier<A[]> attrAnnotations) {
        return DslUtils.getFirstElement(attrAnnotations.get());
    }

    /**
     * 获取指定注解属性注解数组提供者的第一个注解
     *
     * @param attrAnnotations 数组类型属性注解提供方
     * @param <A>             属性注解类型泛型
     * @return 属性注解
     */
    public static <A> A getAnnotationRequired(Supplier<A[]> attrAnnotations) {
        return DslUtils.getFirstElement(attrAnnotations.get()).orElseThrow(AnnotationNotPresentException::new);
    }

    /**
     * 获取指定注解，指定属性类型的注解，如果属性注解是数组，则返回的第零个元素，否则直接返回对应属性注解，可能指定的注解不存在对应类型的属性注解，则返回空
     *
     * @param annotation     注解实例
     * @param attributeClass 属性注解类型
     * @param <A>            属性注解类型泛型
     * @return 属性注解实例
     */
    public static <A> Optional<A> findAttribute(Annotation annotation, Class<A> attributeClass) {
        if (annotation == null || attributeClass == null) {
            return Optional.empty();
        }

        return findMatchMethod(annotation.annotationType(), attributeClass).map(method -> findAttribute(annotation, method));
    }


    public static <A> A findAttribute(Annotation annotation, Method method) {
        Object o = ReflectionUtils.invokeMethod(method, annotation);

        if (o.getClass().isArray()) {
            A[] attributeAnnotations = (A[]) o;
            return attributeAnnotations.length > 0 ? attributeAnnotations[0] : null;
        } else {
            return (A) o;
        }
    }

    public static Optional<Method> findMatchMethod(Class<? extends Annotation> annotationClass, Class<?> attributeClass) {
        return Optional.ofNullable(ANNOTATION_METHODS.computeIfAbsent(annotationClass, AnnotationUtils::createCacheAttributeMethod).get(attributeClass));
    }

    @Synchronized
    private static Map<Class<?>, Method> createCacheAttributeMethod(Class<? extends Annotation> annotationClass) {
        Method[] methods = annotationClass.getDeclaredMethods();
        Map<Class<?>, Method> attributeMethods = new HashMap<>(methods.length);

        Stream.of(methods)
                .filter(AnnotationUtils::isReturnAnnotationOrEnum)
                .forEach(method -> {
                    Class<?> returnType = method.getReturnType();
                    Class<?> attributeAnnotationClass = returnType.isArray() ? returnType.getComponentType() : returnType;
                    attributeMethods.put(attributeAnnotationClass, method);
                });

        ANNOTATION_METHODS.put(annotationClass, attributeMethods);
        return attributeMethods;
    }


    /**
     * 判断指定的方法返回值类型是否是注解类型
     *
     * @param method 方法
     * @return 如果返回值是注解类型，返回<code>true</code>
     */
    public static boolean isReturnAnnotationOrEnum(Method method) {
        Class<?> returnType = method.getReturnType();
        return returnType.isAnnotation() || (returnType.isArray() && returnType.getComponentType().isAnnotation()) || returnType.isEnum();
    }
}

package io.manbang.ebatis.core.meta;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解属性
 *
 * @author 章多亮
 * @since 2020/6/3 13:54
 */
public interface AnnotationAttribute {
    /**
     * 创建指定属性方法的属性元
     *
     * @param method 注解属性方法
     * @return 注解属性
     */
    static AnnotationAttribute of(Method method) {
        return new DefaultAnnotationAttribute(method);
    }

    /**
     * 获取注解属性名称，其实就是方法名称
     *
     * @return 属性名
     */
    String getName();

    /**
     * 判断返回值是否是数组
     *
     * @return 如果是数组，返回<code>true</code>
     */
    boolean isArray();

    /**
     * 判断返回值是否是注解
     *
     * @return 如果是注解，返回<code>true</code>
     */
    boolean isAnnotation();

    /**
     * 判断是否是枚举值
     *
     * @return 如果是枚举，返回<code>true</code>
     */
    boolean isEnum();

    /**
     * 获取属性类型，如果是数组，则是数组元素的类型
     *
     * @return 属性类型
     */
    Class<?> getAttributeType();

    /**
     * 返回指定注解实例的属性值
     *
     * @param instance 注解实例
     * @param <A>      属性类型
     * @return 属性值
     */
    <A> A getValue(Annotation instance);

    /**
     * 返回指定注解实例的属性值，如果属性是数组，则返回第一个值
     *
     * @param instance 注解实例
     * @param <A>      属性类型
     * @return 属性值
     */
    <A> A getFirstValue(Annotation instance);
}

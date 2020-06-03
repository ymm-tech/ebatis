package com.ymm.ebatis.core.meta;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 注解元数据
 *
 * @author 章多亮
 * @since 2020/6/3 13:55
 */
public interface AnnotationMeta extends AnnotatedMeta<Class<? extends Annotation>> {

    /**
     * 创建指定注解的注解元数据
     *
     * @param annotation 注解实例
     * @return 注解元
     */
    static AnnotationMeta of(Annotation annotation) {
        return CachedAnnotationMeta.of(annotation.annotationType());
    }

    /**
     * 创建指定注解类型的元数据
     *
     * @param annotationType 注解类型
     * @return 注解元
     */
    static AnnotationMeta of(Class<? extends Annotation> annotationType) {
        return CachedAnnotationMeta.of(annotationType);
    }


    /**
     * 获取注解属性列表
     *
     * @return 注解属性列表
     */
    List<AnnotationAttribute> getAttributes();

    /**
     * 获取注解类型
     *
     * @return 注解类型
     */
    Class<? extends Annotation> getAnnotationType();
}

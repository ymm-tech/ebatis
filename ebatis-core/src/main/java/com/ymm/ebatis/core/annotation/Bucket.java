package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/2 16:48
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bucket {
    /**
     * 获取分桶聚合类型
     *
     * @return 类型
     */
    BucketType type();

    /**
     * 聚合名称
     *
     * @return name
     */
    String name() default "";

    /**
     * 聚合字段名称
     *
     * @return fieldName
     */
    String fieldName() default "";

    /**
     * 桶聚合顺序
     *
     * @return 捅排序
     */
    BucketOrder[] bucketOrders() default {};
}

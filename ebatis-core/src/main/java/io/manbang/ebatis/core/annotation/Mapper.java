package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/5/22 11:30
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Mapper {
    /**
     * 映射的ES索引列表
     *
     * @return 索引列表
     */
    String[] indices() default {};

    /**
     * 映射的ES类型
     *
     * @return 类型列表
     */
    String[] types() default {};

    /**
     * 集群名称
     *
     * @return 集群名称
     */
    String clusterRouter() default "";
}

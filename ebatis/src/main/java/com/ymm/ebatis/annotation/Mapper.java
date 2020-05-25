package com.ymm.ebatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/5/22 11:30
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
    /**
     * 映射的ES索引列表
     *
     * @return 索引列表
     */
    String[] indices();

    /**
     * 映射的ES类型
     *
     * @return 类型列表
     */
    String[] types() default {};

    /**
     * 获取路由信息
     *
     * @return 路由列表
     */
    String[] routing() default {};
}

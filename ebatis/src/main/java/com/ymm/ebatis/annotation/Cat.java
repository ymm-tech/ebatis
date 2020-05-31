package com.ymm.ebatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * _cat接口
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cat {
    /**
     * 获取要执行的cat操作类型，觉得要发送什么类型的cat请求
     *
     * @return cat操作类型
     */
    CatType catType();

    /**
     * /_cat/aliases
     *
     * @return 别名注解
     */
    Aliases[] aliases() default {};

    Allocation[] allocation() default {};

    /**
     * /_cat/count
     *
     * @return 文档技术注解
     */
    Count[] count() default {};
}

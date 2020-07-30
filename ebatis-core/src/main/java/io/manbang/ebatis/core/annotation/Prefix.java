package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件映射前缀，这样可以见同一组字段放在一个类里面定义
 *
 * @author 章多亮
 * @since 2020/6/4 9:37
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Prefix {

    /**
     * 条件映射前缀
     *
     * @return 前缀
     */
    String value() default "";
}

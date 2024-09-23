package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/8 16:15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Wildcard {
    String rewrite() default "";

    /**
     * 是否大小写不敏感, 默认为大小写不敏感
     *
     * @return <code>true</code> 大小写不敏感, <code>false</code> 大小写敏感
     */
    boolean caseInsensitive() default true;

    float boost() default 1.0f;
}

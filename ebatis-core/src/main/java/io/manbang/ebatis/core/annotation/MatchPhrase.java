package io.manbang.ebatis.core.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/7 10:17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MatchPhrase {

    String analyzer() default "";

    int slop() default 0;

    String zeroTermsQuery() default "";

    float boost() default 1.0f;
}

package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/7 10:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MatchPhrasePrefix {
    int slop() default 0;

    int maxExpansions() default 50;

    String analyzer() default "";
}

package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface MatchBoolPrefix {
    float boost() default 1.0f;

    String minimumShouldMatch() default "";

    String operator() default "or";

    String fuzziness() default "";

    int prefixLength() default 0;

    boolean fuzzyTranspositions() default true;

    int maxExpansions() default 50;

    String fuzzyRewrite() default "";

    String analyzer() default "";
}

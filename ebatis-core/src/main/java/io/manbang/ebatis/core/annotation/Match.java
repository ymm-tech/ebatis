package io.manbang.ebatis.core.annotation;

import org.elasticsearch.index.query.Operator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Match {
    int prefixLength() default 0;

    int maxExpansions() default 50;

    boolean fuzzyTranspositions() default true;

    String minimumShouldMatch() default "";

    String fuzzyRewrite() default "";

    boolean lenient() default false;

    String zeroTermsQuery() default "NONE";

    float cutoffFrequency() default -1;

    boolean autoGenerateSynonymsPhraseQuery() default true;

    Operator operator() default Operator.OR;

    String analyzer() default "";

    String fuzziness() default "AUTO";
}

package io.manbang.ebatis.core.annotation;

import org.apache.lucene.search.FuzzyQuery;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.search.MatchQuery;

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
    int prefixLength() default FuzzyQuery.defaultPrefixLength;

    int maxExpansions() default FuzzyQuery.defaultMaxExpansions;

    boolean fuzzyTranspositions() default FuzzyQuery.defaultTranspositions;

    String minimumShouldMatch() default "";

    String fuzzyRewrite() default "";

    boolean lenient() default MatchQuery.DEFAULT_LENIENCY;

    MatchQuery.ZeroTermsQuery zeroTermsQuery() default MatchQuery.ZeroTermsQuery.NONE;

    float cutoffFrequency() default -1;

    Operator operator() default Operator.OR;

    String analyzer() default "";

    String fuzziness() default "";
}

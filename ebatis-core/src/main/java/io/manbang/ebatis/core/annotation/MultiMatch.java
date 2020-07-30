package io.manbang.ebatis.core.annotation;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.search.MatchQuery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/8 16:16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MultiMatch {

    /**
     * 获取需要匹配的字段列表
     *
     * @return 字段列表
     */
    String[] fields() default {};

    Operator operator() default Operator.OR;

    int slop() default 0;

    String fuzziness() default "AUTO";

    int prefixLength() default 0;

    int maxExpansions() default 50;

    String minimumShouldMatch() default "";

    String fuzzyRewrite() default "";

    float tieBreaker() default 0.0F;

    boolean lenient() default false;

    /**
     * [0..1]，超过这个区间，表示不设置
     */
    float cutoffFrequency() default -1.0F;

    boolean autoGenerateSynonymsPhraseQuery() default true;

    boolean fuzzyTranspositions() default true;

    MatchQuery.ZeroTermsQuery zeroTermsQuery() default MatchQuery.ZeroTermsQuery.NONE;

    MultiMatchQueryBuilder.Type type() default MultiMatchQueryBuilder.Type.BEST_FIELDS;
}

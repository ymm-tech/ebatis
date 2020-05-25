package com.ymm.ebatis.annotation;

import org.apache.lucene.search.FuzzyQuery;
import org.elasticsearch.index.search.MatchQuery;

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
    int slop() default MatchQuery.DEFAULT_PHRASE_SLOP;

    int maxExpansions() default FuzzyQuery.defaultMaxExpansions;

    String analyzer() default AnnotationConstants.NO_SET;
}

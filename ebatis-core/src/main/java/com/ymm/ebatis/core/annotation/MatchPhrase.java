package com.ymm.ebatis.core.annotation;

import org.elasticsearch.index.search.MatchQuery;

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

    int slop() default MatchQuery.DEFAULT_PHRASE_SLOP;

    MatchQuery.ZeroTermsQuery zeroTermsQuery() default MatchQuery.ZeroTermsQuery.NONE;
}

package com.ymm.ebatis.core.annotation;

import org.elasticsearch.action.search.SearchType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 * @since 2019/12/13 17:25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Search {
    QueryType queryType() default QueryType.AUTO;

    SearchType searchType() default SearchType.QUERY_THEN_FETCH;

    String preference() default "";

    String analyzer() default "";

    FunctionScore[] functionScore() default {};

    Match[] match() default {};

    MultiMatch[] multiMatch() default {};

    MatchPhrase[] matchPhrase() default {};

    MatchPhrasePrefix[] matchPhrasePrefix() default {};

    boolean countOnly() default false;
}

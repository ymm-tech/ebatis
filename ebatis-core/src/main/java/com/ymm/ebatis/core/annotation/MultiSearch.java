package com.ymm.ebatis.core.annotation;

import org.elasticsearch.action.search.SearchType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/14 16:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MultiSearch {
    int maxConcurrentSearchRequests() default 0;

    int batchedReduceSize() default 512;

    int preFilterShardSize() default 128;

    boolean countOnly() default false;

    String preference() default "";

    SearchType searchType() default SearchType.QUERY_THEN_FETCH;

    QueryType queryType() default QueryType.AUTO;
}

package io.manbang.ebatis.core.annotation;

import org.elasticsearch.action.search.SearchType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 聚合注解
 *
 * @author duoliang.zhang
 * @since 2019/12/13 17:33
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Agg {
    QueryType queryType() default QueryType.AUTO;

    SearchType searchType() default SearchType.QUERY_THEN_FETCH;

    String preference() default "";

    boolean aggOnly() default false;
}

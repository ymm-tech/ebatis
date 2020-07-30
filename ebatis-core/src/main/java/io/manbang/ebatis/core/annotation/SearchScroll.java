package io.manbang.ebatis.core.annotation;

import org.elasticsearch.action.search.SearchType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Scroll API
 * Retrieves the next batch of results for a scrolling search.
 *
 * @author 章多亮
 * @since 2020/6/8 9:24
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchScroll {
    /**
     * Search context are automatically removed when the scroll timeout has been exceeded.
     * However keeping scrolls open has a cost, as discussed in the previous section so scrolls should be explicitly cleared as soon as the scroll is not being used anymore using the clear-scroll API
     *
     * @return if you want to clear scroll, pls return <code>true</code>
     */
    boolean clearScroll() default false;

    /**
     * specify the scroll parameter in the query string, which tells Elasticsearch how long it should keep the “search context” alive
     *
     * @return initial search request timeout value
     * @see org.elasticsearch.common.unit.TimeValue
     */
    String initialKeepAlive() default "1m";

    /**
     * If no scroll value is set for the SearchScrollRequest, the search context will expire once the initial scroll time expired (ie, the scroll time set in the initial search request).
     *
     * @return scroll request timeout value
     * @see org.elasticsearch.common.unit.TimeValue
     */
    String keepAlive() default "1m";

    QueryType queryType() default QueryType.AUTO;

    SearchType searchType() default SearchType.QUERY_THEN_FETCH;

    String preference() default "";

    String analyzer() default "";

    FunctionScore[] functionScore() default {};
}

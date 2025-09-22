package io.manbang.ebatis.core.annotation;

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

    String timeout() default "1m";

    boolean trackTotalHits() default false;

    /**
     * 可选的值：true|false|null|""
     *
     * @return 默认值：""
     */
    String allowPartialSearchResults() default "";

    /**
     * 为单个请求显式地启用或禁用分片级别的请求缓存。
     * <p>
     * 可选的值：true|false|null|""
     *
     * @return 默认值: ""
     */
    String requestCache() default "";

    String scrollKeepAlive() default "";
}

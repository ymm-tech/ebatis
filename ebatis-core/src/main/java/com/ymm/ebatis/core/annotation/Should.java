package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 * @since 2019/12/13 17:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Should {
    /**
     * @return 是否是嵌套条件
     */
    boolean nested() default false;

    /**
     * @return 最小匹配度
     */
    String minimumShouldMatch() default "";

    /**
     * @return 百分比形式，返回<code>true</code>
     */
    boolean percent() default false;

    QueryType queryType() default QueryType.AUTO;

    Bool[] bool() default {};

    Boosting[] boosting() default {};

    DisMax[] disMax() default {};

    Fuzzy[] fuzzy() default {};

    FunctionScore[] functionScore() default {};

    GeoBoundingBox[] geoBoundingBox() default {};

    GeoDistance[] geoDistance() default {};

    GeoPolygon[] geoPolygon() default {};

    GeoShape[] geoShape() default {};

    Ids[] ids() default {};

    Match[] match() default {};

    MatchAll[] matchAll() default {};

    MatchPhrase[] matchPhrase() default {};

    MatchPhrasePrefix[] matchPhrasePrefix() default {};

    MultiMatch[] multiMatch() default {};

    Nested[] nest() default {};

    Range[] range() default {};

    Script[] script() default {};

    SpanContaining[] spanContaining() default {};

    SpanFirst[] spanFirst() default {};

    SpanNear[] spanNear() default {};

    SpanNot[] spanNot() default {};

    SpanOr[] spanOr() default {};

    SpanTerm[] spanTerm() default {};

    SpanWithin[] spanWithin() default {};

    Term[] term() default {};

    Terms[] terms() default {};

    Wildcard[] wildcard() default {};
}

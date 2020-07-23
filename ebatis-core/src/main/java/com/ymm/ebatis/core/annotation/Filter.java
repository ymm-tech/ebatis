package com.ymm.ebatis.core.annotation;

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
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Filter {
    boolean nested() default false;

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

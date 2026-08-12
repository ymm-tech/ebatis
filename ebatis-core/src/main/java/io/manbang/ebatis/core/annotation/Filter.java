package io.manbang.ebatis.core.annotation;

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

    float boost() default 1.0f;

    QueryType queryType() default QueryType.AUTO;

    Bool bool() default @Bool;

    Boosting boosting() default @Boosting;

    DisMax disMax() default @DisMax;

    Exists exists() default @Exists;

    Fuzzy fuzzy() default @Fuzzy;

    FunctionScore functionScore() default @FunctionScore;

    GeoBoundingBox geoBoundingBox() default @GeoBoundingBox;

    GeoDistance geoDistance() default @GeoDistance;

    GeoPolygon geoPolygon() default @GeoPolygon;

    GeoShape geoShape() default @GeoShape;

    Ids ids() default @Ids;

    Match match() default @Match;

    MatchAll matchAll() default @MatchAll;

    MatchPhrase matchPhrase() default @MatchPhrase;

    MatchPhrasePrefix matchPhrasePrefix() default @MatchPhrasePrefix;

    MatchBoolPrefix matchBoolPrefix() default @MatchBoolPrefix;

    Prefix prefix() default @Prefix;

    MultiMatch multiMatch() default @MultiMatch;

    Nested nest() default @Nested(path = "");

    Range range() default @Range;

    Script script() default @Script;

    SpanContaining spanContaining() default @SpanContaining;

    SpanFirst spanFirst() default @SpanFirst;

    SpanNear spanNear() default @SpanNear;

    SpanNot spanNot() default @SpanNot;

    SpanOr spanOr() default @SpanOr;

    SpanTerm spanTerm() default @SpanTerm;

    SpanWithin spanWithin() default @SpanWithin;

    Term term() default @Term;

    Terms terms() default @Terms;

    Wildcard wildcard() default @Wildcard;
}

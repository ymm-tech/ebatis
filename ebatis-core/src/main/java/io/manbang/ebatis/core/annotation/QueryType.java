package io.manbang.ebatis.core.annotation;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import io.manbang.ebatis.core.meta.FieldMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author duoliang.zhang
 */

public enum QueryType {
    /**
     * 自动匹配
     */
    AUTO(QueryBuilderFactory.auto()),
    /**
     * Bool组合查询
     */
    BOOL(QueryBuilderFactory.bool()),
    /**
     * 函数积分组合查询
     */
    FUNCTION_SCORE(QueryBuilderFactory.functionScore()),
    /**
     * 常量积分组合查询
     */
    CONSTANT_SCORE(QueryBuilderFactory.constantScore()),
    /**
     * Ids查询
     */
    BOOSTING(QueryBuilderFactory.boosting()),
    DIS_MAX(QueryBuilderFactory.disMax()),
    FIELD(null),
    FUZZY(QueryBuilderFactory.fuzzy()),
    GEO_SHAPE(QueryBuilderFactory.geoShape()),
    GEO_DISTANCE(QueryBuilderFactory.geoDistance()),
    GEO_POLYGON(null),
    GEO_BOUNDING_BOX(null),
    HAS_CHILD(null),
    HAS_PARENT(null),
    INDICES(null),
    MLT(null),
    MULTI_MATCH(QueryBuilderFactory.multiMatch()),
    NESTED(QueryBuilderFactory.nested()),
    PREFIX(null),
    QUERY_STRING(null),
    RANGE(QueryBuilderFactory.auto()),
    SCRIPT(null),
    IDS(null),
    TERM(QueryBuilderFactory.term()),
    TERMS(QueryBuilderFactory.terms()),
    EXISTS(QueryBuilderFactory.exists()),
    MATCH_ALL(QueryBuilderFactory.matchAll()),
    MATCH(QueryBuilderFactory.match()),
    MATCH_PHRASE(QueryBuilderFactory.matchPhrase()),
    MATCH_PHRASE_PREFIX(QueryBuilderFactory.matchPhrasePrefix()),
    SPAN_CONTAINING(null),
    SPAN_FIRST(null),
    SPAN_NEAR(null),
    SPAN_NOT(null),
    SPAN_OR(null),
    SPAN_TERM(null),
    SPAN_WITHIN(null),
    WILDCARD(QueryBuilderFactory.wildCard());

    private final QueryBuilderFactory queryBuilderFactory;

    QueryType(QueryBuilderFactory queryBuilderFactory) {
        this.queryBuilderFactory = queryBuilderFactory;
    }

    public QueryBuilderFactory getQueryBuilderFactory() {
        return queryBuilderFactory;
    }

    public QueryBuilder createBuilder(FieldMeta fieldMeta, Object value) {
        return queryBuilderFactory.create(fieldMeta, value);
    }
}

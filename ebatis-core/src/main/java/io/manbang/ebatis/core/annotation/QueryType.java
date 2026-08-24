package io.manbang.ebatis.core.annotation;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.FieldMeta;
import lombok.Getter;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author duoliang.zhang
 */

@Getter
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
    GEO_POLYGON(QueryBuilderFactory.geoPolygon()),
    GEO_BOUNDING_BOX(QueryBuilderFactory.geoBoundingBox()),
    HAS_CHILD(QueryBuilderFactory.hasChild()),
    HAS_PARENT(QueryBuilderFactory.hasParent()),
    INDICES(null),
    MLT(QueryBuilderFactory.moreLikeThis()),
    MULTI_MATCH(QueryBuilderFactory.multiMatch()),
    NESTED(QueryBuilderFactory.nested()),
    PREFIX(QueryBuilderFactory.prefix()),
    QUERY_STRING(QueryBuilderFactory.queryString()),
    SIMPLE_QUERY_STRING(QueryBuilderFactory.simpleQueryString()),
    RANGE(QueryBuilderFactory.auto()),
    SCRIPT(QueryBuilderFactory.script()),
    REGEXP(QueryBuilderFactory.regexp()),
    WRAPPER(QueryBuilderFactory.wrapper()),
    IDS(QueryBuilderFactory.ids()),
    TERM(QueryBuilderFactory.term()),
    TERMS(QueryBuilderFactory.terms()),
    EXISTS(QueryBuilderFactory.exists()),
    MATCH_ALL(QueryBuilderFactory.matchAll()),
    MATCH(QueryBuilderFactory.match()),
    MATCH_PHRASE(QueryBuilderFactory.matchPhrase()),
    MATCH_PHRASE_PREFIX(QueryBuilderFactory.matchPhrasePrefix()),
    MATCH_BOOL_PREFIX(QueryBuilderFactory.matchBoolPrefix()),
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

    public QueryBuilder createBuilder(FieldMeta fieldMeta, Object value) {
        if (queryBuilderFactory == null) {
            throw new ConditionNotSupportException("QueryType " + name() + " is not implemented");
        }
        return queryBuilderFactory.create(fieldMeta, value);
    }

    public boolean isSupported() {
        return queryBuilderFactory != null;
    }
}

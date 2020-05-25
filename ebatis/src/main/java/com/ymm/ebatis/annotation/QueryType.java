package com.ymm.ebatis.annotation;

import com.ymm.ebatis.meta.FieldConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author duoliang.zhang
 */

public enum QueryType {
    /**
     * 自动匹配
     */
    AUTO,
    /**
     * Bool组合查询
     */
    BOOL,
    /**
     * 函数积分组合查询
     */
    FUNCTION_SCORE,
    /**
     * 常量积分组合查询
     */
    CONSTANT_SCORE,
    /**
     * Ids查询
     */
    BOOSTING,
    DIS_MAX,
    FIELD,
    FUZZY,
    GEO_SHAPE,
    GEO_DISTANCE,
    GEO_POLYGON,
    GEO_BOUNDING_BOX,
    HAS_CHILD,
    HAS_PARENT,
    INDICES,
    MLT,
    MULTI_MATCH,
    NESTED,
    PREFIX,
    QUERY_STRING,
    RANGE,
    SCRIPT,
    IDS,
    TERM,
    TERMS,
    EXISTS,
    MATCH_ALL,
    MATCH,
    MATCH_PHRASE,
    MATCH_PHRASE_PREFIX,
    SPAN_CONTAINING,
    SPAN_FIRST,
    SPAN_NEAR,
    SPAN_NOT,
    SPAN_OR,
    SPAN_TERM,
    SPAN_WITHIN,
    WILDCARD;

    public QueryBuilder createBuilder(FieldConditionMeta conditionMeta, Object v) {
        return null;
    }
}

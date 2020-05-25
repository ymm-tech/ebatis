package com.ymm.ebatis.annotation;

/**
 * @author 章多亮
 * @since 2020/1/3 13:13
 */
public enum BucketType {
    /**
     * 邻接矩阵
     */
    ADJACENCY_MATRIX,
    CHILDREN,
    COMPOSITE,
    DATE_HISTOGRAM,
    DATE_RANGE,
    DIVERSIFIED_SAMPLER,
    FILTER,
    FILTERS,
    GEO_DISTANCE,
    GEO_HASH_GRID,
    GLOBAL,
    HISTOGRAM,
    IP_RANGE,
    MISSING,
    NESTED,
    RANGE,
    REVERSER_NESTED,
    SAMPLER,
    SIGNIFICANT_TERMS,
    SIGNIFICANT_TEXT,
    TERMS
}

package com.ymm.ebatis.core.annotation;

/**
 * metric聚合类型，单值聚合类型
 *
 * @author 章多亮
 * @since 2020/1/2 16:39
 */
public enum MetricType {
    /**
     * 平均值
     */
    AVG,
    /**
     * 待权重的平均值
     */
    WEIGHTED_AVG,
    CARDINALITY,
    EXTENDED_STATS,
    GEO_BOUNDS,
    GEO_CENTROID,
    /**
     * 最大值
     */
    MAX,
    /**
     * 最小值
     */
    MIN, PERCENTILES, PERCENTILE_RANKS,
    SCRIPTED_METRIC, STATS,
    /**
     * 求和
     */
    SUM, TOP_HITS,
    /**
     * 计数
     */
    VALUE_COUNT
}

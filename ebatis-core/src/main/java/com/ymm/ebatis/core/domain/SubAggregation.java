package com.ymm.ebatis.core.domain;

/**
 * @author weilong.hu
 */
public interface SubAggregation<T extends SubAggregation<T>> extends Aggregation {
    /**
     * 子聚合
     *
     * @param aggs 子聚合
     * @return T
     */
    T subAgg(Aggregation... aggs);
}

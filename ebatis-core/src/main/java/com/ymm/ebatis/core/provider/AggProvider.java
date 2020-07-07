package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.Aggregation;

/**
 * 聚合条件提供者
 *
 * @author 章多亮
 * @since 2020/1/2 19:51
 */
public interface AggProvider extends Provider {
    /**
     * 获取多聚合
     *
     * @return 多个聚合
     */
    default Aggregation[] getAggregations() {
        Aggregation agg = getAggregation();
        return agg == null ? new Aggregation[0] : new Aggregation[]{agg};
    }

    /**
     * 获取单聚合
     *
     * @return 单个聚合
     */
    default Aggregation getAggregation() {
        return null;
    }
}

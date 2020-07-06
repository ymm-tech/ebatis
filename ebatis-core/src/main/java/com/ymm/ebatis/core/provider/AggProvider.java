package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.Aggregation;

import java.util.Collections;
import java.util.List;

/**
 * 聚合条件提供者
 *
 * @author 章多亮
 * @since 2020/1/2 19:51
 */
public interface AggProvider extends Provider {
    /**
     * 获取所搜条件
     *
     * @return 搜索条件
     */
    default Object getCondition() {
        return this;
    }

    /**
     * 获取多聚合
     *
     * @return 多个聚合
     */
    default List<Aggregation> getAggregations() {
        Aggregation agg = getAggregation();
        return agg == null ? Collections.emptyList() : Collections.singletonList(agg);
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

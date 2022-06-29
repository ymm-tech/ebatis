package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.exception.AggregationInitializationException;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * @author weilong.hu
 * @since 2022/06/29 11:20
 */
public class MinAggregation implements SubAggregation<MinAggregation>, BuildProvider {
    /**
     * 聚合名称
     */
    private String name;

    /**
     * 聚合字段名称
     */
    private String fieldName;

    MinAggregation(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public MinAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public MinAggregation subAgg(Aggregation... aggs) {
        throw new AggregationInitializationException("Aggregator [" + name + "] of type [min] cannot accept sub-aggregations");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) AggregationBuilders.min(name).field(fieldName);
    }
}

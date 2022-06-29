package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.exception.AggregationInitializationException;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * @author weilong.hu
 * @since 2022/06/29 11:20
 */
public class MaxAggregation implements SubAggregation<MaxAggregation>, BuildProvider {
    /**
     * 聚合名称
     */
    private String name;

    /**
     * 聚合字段名称
     */
    private String fieldName;

    MaxAggregation(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public MaxAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public MaxAggregation subAgg(Aggregation... aggs) {
        throw new AggregationInitializationException("Aggregator [" + name + "] of type [max] cannot accept sub-aggregations");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) AggregationBuilders.max(name).field(fieldName);
    }
}

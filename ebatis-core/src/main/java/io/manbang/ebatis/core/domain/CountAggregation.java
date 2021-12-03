package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.exception.AggregationInitializationException;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * @author weilong.hu
 * @since 2021/06/23 09:34
 */
public class CountAggregation implements SubAggregation<CountAggregation>, BuildProvider {
    /**
     * 聚合名称
     */
    private final String name;
    /**
     * 聚合字段名称
     */
    private String fieldName;

    public CountAggregation(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public CountAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    public CountAggregation subAgg(Aggregation... aggs) {
        throw new AggregationInitializationException("Aggregator [" + name + "] of type [value_count] cannot accept sub-aggregations");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) AggregationBuilders.count(name).field(fieldName);
    }
}

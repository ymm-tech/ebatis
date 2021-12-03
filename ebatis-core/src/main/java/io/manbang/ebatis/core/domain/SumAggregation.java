package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2021/5/21 11:04
 */
public class SumAggregation implements SubAggregation<SumAggregation>, BuildProvider {
    /**
     * 聚合名称
     */
    private final String name;
    /**
     * 子聚合
     */
    private final List<Aggregation> subAggregations = new ArrayList<>();
    /**
     * 聚合字段名称
     */
    private String fieldName;

    public SumAggregation(String name) {
        this.name = name;
    }

    @Override
    public SumAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SumAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        final SumAggregationBuilder sum = AggregationBuilders.sum(name).field(fieldName);
        subAggregations.forEach(sub -> {
            final AggregationBuilder aggregationBuilder = ((BuildProvider) sub).build();
            sum.subAggregation(aggregationBuilder);
        });
        return (T) sum;
    }
}

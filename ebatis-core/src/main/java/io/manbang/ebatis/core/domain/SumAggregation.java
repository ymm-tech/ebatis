package io.manbang.ebatis.core.domain;

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
public class SumAggregation implements SubAggregation<SumAggregation> {
    /**
     * 聚合名称
     */
    private String name;
    /**
     * 聚合字段名称
     */
    private String fieldName;
    /**
     * 子聚合
     */
    private List<Aggregation> subAggregations = new ArrayList<>();

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
    public AggregationBuilder toAggBuilder() {
        final SumAggregationBuilder sum = AggregationBuilders.sum(name).field(fieldName);
        subAggregations.forEach(sub -> sum.subAggregation(sub.toAggBuilder()));
        return sum;
    }
}

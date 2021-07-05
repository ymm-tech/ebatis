package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/07/05 10:09
 */
public class AvgAggregation implements SubAggregation<AvgAggregation> {
    /**
     * 聚合名称
     */
    private final String name;
    /**
     * 聚合字段名称
     */
    private String fieldName;

    /**
     * 缺失参数定义了应该如何处理缺少值的文档。
     * 默认情况下，它们将被忽略，但也可以将它们视为有值。
     */
    private Object missing;
    /**
     * 子聚合
     */
    private final List<Aggregation> subAggregations = new ArrayList<>();

    public AvgAggregation(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public AvgAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Object getMissing() {
        return missing;
    }

    public AvgAggregation missing(Object missing) {
        this.missing = missing;
        return this;
    }

    @Override
    public AvgAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    @Override
    public AggregationBuilder toAggBuilder() {
        final AvgAggregationBuilder avg = AggregationBuilders.avg(name).field(fieldName);
        if (Objects.nonNull(missing)) {
            avg.missing(missing);
        }
        subAggregations.forEach(sub -> avg.subAggregation(sub.toAggBuilder()));
        return avg;
    }
}

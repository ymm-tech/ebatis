package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.PercentileRanksAggregationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/07/21 10:06
 */
public class PercentileRanksAggregation implements SubAggregation<PercentileRanksAggregation> {
    /**
     * 聚合名称
     */
    private final String name;
    private final double[] values;
    /**
     * 聚合字段名称
     */
    private String fieldName;
    private double compression = 100.0;
    private boolean keyed = true;
    private Object missing;
    private Script script;
    /**
     * 子聚合
     */
    private final List<Aggregation> subAggregations = new ArrayList<>();

    public PercentileRanksAggregation(String name, double[] values) {
        if (values == null) {
            throw new IllegalArgumentException("[values] must not be null: [" + name + "]");
        }
        if (values.length == 0) {
            throw new IllegalArgumentException("[values] must not be an empty array: [" + name + "]");
        }
        this.values = values;
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public PercentileRanksAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public PercentileRanksAggregation keyed(boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public PercentileRanksAggregation compression(double compression) {
        if (compression < 0.0) {
            throw new IllegalArgumentException(
                    "[compression] must be greater than or equal to 0. Found [" + compression + "] in [" + name + "]");
        }
        this.compression = compression;
        return this;
    }

    public PercentileRanksAggregation missing(Object missing) {
        this.missing = missing;
        return this;
    }

    public PercentileRanksAggregation script(Script script) {
        this.script = script;
        return this;
    }

    @Override
    public PercentileRanksAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    @Override
    public AggregationBuilder toAggBuilder() {
        final PercentileRanksAggregationBuilder percentileRanks = AggregationBuilders.percentileRanks(name, values);
        percentileRanks.field(fieldName);
        percentileRanks.compression(compression);
        percentileRanks.keyed(keyed);
        if (!subAggregations.isEmpty()) {
            subAggregations.forEach(subAgg -> percentileRanks.subAggregation(subAgg.toAggBuilder()));
        }
        if (Objects.nonNull(missing)) {
            percentileRanks.missing(missing);
        }
        if (Objects.nonNull(script)) {
            percentileRanks.script(script.toEsScript());
        }
        return percentileRanks;
    }
}

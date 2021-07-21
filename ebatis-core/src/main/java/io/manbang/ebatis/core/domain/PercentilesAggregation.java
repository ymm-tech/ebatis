package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2021/07/21 09:38
 */
public class PercentilesAggregation implements SubAggregation<PercentilesAggregation> {
    /**
     * 聚合名称
     */
    private final String name;
    /**
     * 聚合字段名称
     */
    private String fieldName;

    private static final double[] DEFAULT_PERCENTS = new double[]{1, 5, 25, 50, 75, 95, 99};
    private double[] percents = DEFAULT_PERCENTS;
    private double compression = 100.0;
    private boolean keyed = true;
    /**
     * 子聚合
     */
    private final List<Aggregation> subAggregations = new ArrayList<>();

    public PercentilesAggregation(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public PercentilesAggregation fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public PercentilesAggregation keyed(boolean keyed) {
        this.keyed = keyed;
        return this;
    }

    public PercentilesAggregation percentiles(double... percents) {
        if (percents == null) {
            throw new IllegalArgumentException("[percents] must not be null: [" + name + "]");
        }
        if (percents.length == 0) {
            throw new IllegalArgumentException("[percents] must not be empty: [" + name + "]");
        }
        this.percents = percents;
        return this;
    }

    public PercentilesAggregation compression(double compression) {
        if (compression < 0.0) {
            throw new IllegalArgumentException(
                    "[compression] must be greater than or equal to 0. Found [" + compression + "] in [" + name + "]");
        }
        this.compression = compression;
        return this;
    }

    @Override
    public PercentilesAggregation subAgg(Aggregation... aggs) {
        Collections.addAll(subAggregations, aggs);
        return this;
    }

    @Override
    public AggregationBuilder toAggBuilder() {
        final PercentilesAggregationBuilder percentiles = AggregationBuilders.percentiles(name).field(fieldName);
        percentiles.percentiles(percents);
        percentiles.compression(compression);
        percentiles.keyed(keyed);
        if (!subAggregations.isEmpty()) {
            subAggregations.forEach(subAgg -> percentiles.subAggregation(subAgg.toAggBuilder()));
        }
        return percentiles;
    }
}

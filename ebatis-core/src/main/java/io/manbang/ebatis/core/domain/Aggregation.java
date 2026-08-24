package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import java.util.List;

/**
 * @author weilong.hu
 */
public interface Aggregation {
    static NativeAggregation histogram(String name, String field, double interval) {
        return new NativeAggregation(AggregationBuilders.histogram(name).field(field).interval(interval));
    }

    static NativeAggregation dateHistogram(String name, String field, String calendarInterval) {
        return new NativeAggregation(AggregationBuilders.dateHistogram(name).field(field)
                .calendarInterval(new DateHistogramInterval(calendarInterval)));
    }

    static NativeAggregation range(String name, String field, double... bounds) {
        org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder builder =
                AggregationBuilders.range(name).field(field);
        if (bounds != null && bounds.length > 0) {
            builder.addUnboundedTo(bounds[0]);
            for (int i = 0; i + 1 < bounds.length; i++) {
                builder.addRange(bounds[i], bounds[i + 1]);
            }
            builder.addUnboundedFrom(bounds[bounds.length - 1]);
        }
        return new NativeAggregation(builder);
    }

    static NativeAggregation filter(String name, org.elasticsearch.index.query.QueryBuilder filter) {
        return new NativeAggregation(AggregationBuilders.filter(name, filter));
    }

    static NativeAggregation nested(String name, String path) {
        return new NativeAggregation(AggregationBuilders.nested(name, path));
    }

    static NativeAggregation cardinality(String name, String field) {
        return new NativeAggregation(AggregationBuilders.cardinality(name).field(field));
    }

    static NativeAggregation stats(String name, String field) {
        return new NativeAggregation(AggregationBuilders.stats(name).field(field));
    }

    static NativeAggregation topHits(String name, int size) {
        return new NativeAggregation(AggregationBuilders.topHits(name).size(size));
    }

    static NativeAggregation composite(String name, List<CompositeValuesSourceBuilder<?>> sources, int size) {
        return new NativeAggregation(AggregationBuilders.composite(name, sources).size(size));
    }
    /**
     * Create a new Terms aggregation with the given name.
     *
     * @param name name
     * @return TermsAggregation
     */
    static TermsAggregation terms(String name) {
        return new TermsAggregation(name);
    }

    /**
     * Create a new Filters aggregation with the given name.
     *
     * @param name name
     * @return FiltersAggregation
     */
    static FiltersAggregation filters(String name) {
        return new FiltersAggregation(name);
    }

    /**
     * Create a new Sum aggregation with the given name.
     *
     * @param name name
     * @return SumAggregation
     */
    static SumAggregation sum(String name) {
        return new SumAggregation(name);
    }

    /**
     * Create a new ValueCount aggregation with the given name.
     *
     * @param name name
     * @return CountAggregation
     */
    static CountAggregation count(String name) {
        return new CountAggregation(name);
    }

    /**
     * Create a new avg aggregation with the given name.
     *
     * @param name name
     * @return CountAggregation
     */
    static AvgAggregation avg(String name) {
        return new AvgAggregation(name);
    }

    /**
     * Create a new percentiles aggregation with the given name.
     *
     * @param name name
     * @return PercentilesAggregation
     */
    static PercentilesAggregation percentiles(String name) {
        return new PercentilesAggregation(name);
    }

    /**
     * Create a new PercentileRanks aggregation with the given name.
     *
     * @param name   name
     * @param values values
     * @return PercentileRanksAggregation
     */
    static PercentileRanksAggregation percentileRanks(String name, double... values) {
        return new PercentileRanksAggregation(name, values);
    }

    /**
     * Create a new Min aggregation with the given name.
     *
     * @param name name
     * @return MinAggregation
     */
    static MinAggregation min(String name) {
        return new MinAggregation(name);
    }

    /**
     * Create a new Max aggregation with the given name.
     *
     * @param name name
     * @return MaxAggregation
     */
    static MaxAggregation max(String name) {
        return new MaxAggregation(name);
    }

}

package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * @author weilong.hu
 */
public interface Aggregation {
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
     * builder
     *
     * @return builder
     */
    AggregationBuilder toAggBuilder();
}

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
     * builder
     *
     * @return builder
     */
    AggregationBuilder toAggBuilder();
}

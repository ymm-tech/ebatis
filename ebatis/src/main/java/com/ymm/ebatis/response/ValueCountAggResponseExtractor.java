package com.ymm.ebatis.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.ValueCount;

/**
 * @author 章多亮
 * @since 2020/1/3 11:22
 */
public class ValueCountAggResponseExtractor implements MetricSearchResponseExtractor<Long> {
    public static final ValueCountAggResponseExtractor INSTANCE = new ValueCountAggResponseExtractor();

    private ValueCountAggResponseExtractor() {
    }

    @Override
    public Long doExtractData(SearchResponse response) {
        return ((ValueCount) response.getAggregations().iterator().next()).getValue();
    }
}

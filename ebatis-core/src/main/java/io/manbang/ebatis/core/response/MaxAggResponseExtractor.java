package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.max.Max;

/**
 * @author 章多亮
 * @since 2020/1/3 12:45
 */
public class MaxAggResponseExtractor implements MetricSearchResponseExtractor<Double> {
    public static final MaxAggResponseExtractor INSTANCE = new MaxAggResponseExtractor();

    private MaxAggResponseExtractor() {
    }

    @Override
    public Double doExtractData(SearchResponse response) {
        return ((Max) response.getAggregations().iterator().next()).getValue();
    }
}

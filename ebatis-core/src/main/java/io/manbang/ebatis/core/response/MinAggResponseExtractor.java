package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.min.Min;

/**
 * @author 章多亮
 * @since 2020/1/3 12:36
 */
public class MinAggResponseExtractor implements MetricSearchResponseExtractor<Double> {
    public static final MinAggResponseExtractor INSTANCE = new MinAggResponseExtractor();

    private MinAggResponseExtractor() {
    }

    @Override
    public Double doExtractData(SearchResponse response) {
        return ((Min) response.getAggregations().iterator().next()).getValue();
    }
}

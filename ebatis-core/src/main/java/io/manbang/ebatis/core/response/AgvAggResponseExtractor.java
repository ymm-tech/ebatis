package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.Avg;

/**
 * @author 章多亮
 * @since 2020/1/3 12:07
 */
public class AgvAggResponseExtractor implements MetricSearchResponseExtractor<Double> {
    public static final AgvAggResponseExtractor INSTANCE = new AgvAggResponseExtractor();

    private AgvAggResponseExtractor() {
    }

    @Override
    public Double doExtractData(SearchResponse response) {
        return ((Avg) response.getAggregations().iterator().next()).getValue();
    }
}

package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

/**
 * @author 章多亮
 * @since 2020/1/3 12:35
 */
public class SumAggResponseExtractor implements MetricSearchResponseExtractor<Double> {
    public static final SumAggResponseExtractor INSTANCE = new SumAggResponseExtractor();

    private SumAggResponseExtractor() {
    }

    @Override
    public Double doExtractData(SearchResponse response) {
        return ((Sum) response.getAggregations().iterator().next()).getValue();
    }
}

package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;

/**
 * @author 章多亮
 * @since 2020/1/3 11:06
 */
public class AggregationsResponseExtractor implements MetricSearchResponseExtractor<Aggregations> {
    public static final AggregationsResponseExtractor INSTANCE = new AggregationsResponseExtractor();

    private AggregationsResponseExtractor() {
    }

    @Override
    public Aggregations doExtractData(SearchResponse response) {
        return response.getAggregations();
    }
}

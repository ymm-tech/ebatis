package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/3 11:13
 */
public class AggregationListResponseExtractor implements MetricSearchResponseExtractor<List<Aggregation>> {
    public static final AggregationListResponseExtractor INSTANCE = new AggregationListResponseExtractor();

    private AggregationListResponseExtractor() {
    }

    @Override
    public List<Aggregation> doExtractData(SearchResponse response) {
        return response.getAggregations().asList();
    }
}

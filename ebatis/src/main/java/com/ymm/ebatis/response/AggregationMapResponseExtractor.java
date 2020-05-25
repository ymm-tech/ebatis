package com.ymm.ebatis.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

/**
 * @author 章多亮
 * @since 2020/1/3 11:15
 */
public class AggregationMapResponseExtractor implements MetricSearchResponseExtractor<Map<String, Aggregation>> {
    public static final AggregationMapResponseExtractor INSTANCE = new AggregationMapResponseExtractor();

    private AggregationMapResponseExtractor() {
    }

    @Override
    public Map<String, Aggregation> doExtractData(SearchResponse response) {
        return response.getAggregations().asMap();
    }
}

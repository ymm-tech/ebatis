package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.metrics.Cardinality;

/**
 * @author 章多亮
 * @since 2020/1/3 12:32
 */
public class CardinalityAggResponseExtractor implements MetricSearchResponseExtractor<Long> {
    @Override
    public Long doExtractData(SearchResponse response) {
        return ((Cardinality) response.getAggregations().iterator().next()).getValue();
    }
}

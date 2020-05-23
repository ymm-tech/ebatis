package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;

/**
 * @author 章多亮
 * @since 2020/1/17 14:54
 */
public class TotalHitsSearchResponseExtractor implements SearchResponseExtractor<Long> {
    public static final TotalHitsSearchResponseExtractor INSTANCE = new TotalHitsSearchResponseExtractor();

    private TotalHitsSearchResponseExtractor() {
    }

    @Override
    public Long doExtractData(SearchResponse response) {
        return response.getHits().getTotalHits().value;
    }
}

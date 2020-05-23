package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/14 16:45
 */
public class TotalHitsArrayMultiSearchResponseExtractor implements MultiSearchResponseExtractor<Long[]> {
    public static final TotalHitsArrayMultiSearchResponseExtractor INSTANCE = new TotalHitsArrayMultiSearchResponseExtractor();

    private TotalHitsArrayMultiSearchResponseExtractor() {
    }

    @Override
    public Long[] doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .toArray(Long[]::new);
    }
}

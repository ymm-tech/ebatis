package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.util.stream.Stream;

/**
 * @author weilong.hu
 * @since 2020/7/7 15:24
 */
public class TotalHitsBasicArrayMultiSearchResponseExtractor implements MultiSearchResponseExtractor<long[]> {
    public static final TotalHitsBasicArrayMultiSearchResponseExtractor INSTANCE = new TotalHitsBasicArrayMultiSearchResponseExtractor();

    private TotalHitsBasicArrayMultiSearchResponseExtractor() {
    }

    @Override
    public long[] doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .mapToLong(totalHits -> totalHits.value)
                .toArray();
    }
}
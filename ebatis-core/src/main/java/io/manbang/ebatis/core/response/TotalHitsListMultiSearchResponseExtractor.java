package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/14 17:11
 */
public class TotalHitsListMultiSearchResponseExtractor implements MultiSearchResponseExtractor<List<Long>> {
    public static final TotalHitsListMultiSearchResponseExtractor INSTANCE = new TotalHitsListMultiSearchResponseExtractor();

    private TotalHitsListMultiSearchResponseExtractor() {
    }

    @Override
    public List<Long> doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .map(totalHits -> totalHits.value)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> empty() {
        return Collections.emptyList();
    }
}

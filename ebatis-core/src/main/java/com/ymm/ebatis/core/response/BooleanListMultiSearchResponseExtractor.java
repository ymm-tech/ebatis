package com.ymm.ebatis.core.response;

import org.apache.commons.lang3.math.NumberUtils;
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
public class BooleanListMultiSearchResponseExtractor implements MultiSearchResponseExtractor<List<Boolean>> {
    public static final BooleanListMultiSearchResponseExtractor INSTANCE = new BooleanListMultiSearchResponseExtractor();

    private BooleanListMultiSearchResponseExtractor() {
    }

    @Override
    public List<Boolean> doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .map(totalHits -> !NumberUtils.LONG_ZERO.equals(totalHits.value))
                .collect(Collectors.toList());
    }

    @Override
    public List<Boolean> empty() {
        return Collections.emptyList();
    }
}

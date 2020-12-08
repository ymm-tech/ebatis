package io.manbang.ebatis.core.response;

import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.util.stream.Stream;

/**
 * @author weilong.hu
 * @since 2020/7/7 17:02
 */
public class BooleanArrayMultiSearchResponseExtractor implements MultiSearchResponseExtractor<Boolean[]> {
    public static final BooleanArrayMultiSearchResponseExtractor INSTANCE = new BooleanArrayMultiSearchResponseExtractor();

    @Override
    public Boolean[] doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .map(totalHits -> !NumberUtils.LONG_ZERO.equals(totalHits))
                .toArray(Boolean[]::new);
    }

    @Override
    public Boolean[] empty() {
        return new Boolean[0];
    }
}

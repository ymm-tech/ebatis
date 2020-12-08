package io.manbang.ebatis.core.response;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import java.util.stream.Stream;

/**
 * @author weilong.hu
 * @since 2020/7/7 17:02
 */
public class BoolArrayMultiSearchResponseExtractor implements MultiSearchResponseExtractor<boolean[]> {
    public static final BoolArrayMultiSearchResponseExtractor INSTANCE = new BoolArrayMultiSearchResponseExtractor();

    @Override
    public boolean[] doExtractData(MultiSearchResponse response) {
        Boolean[] booleans = Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(SearchResponse::getHits)
                .map(SearchHits::getTotalHits)
                .map(totalHits -> !NumberUtils.LONG_ZERO.equals(totalHits))
                .toArray(Boolean[]::new);
        return ArrayUtils.toPrimitive(booleans);
    }

    @Override
    public boolean[] empty() {
        return new boolean[0];
    }
}
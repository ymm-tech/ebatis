package com.ymm.ebatis.response;

import org.elasticsearch.action.search.MultiSearchResponse;

/**
 * @author 章多亮
 * @since 2020/1/14 16:43
 */
public class RawMultiSearchResponseExtractor implements MultiSearchResponseExtractor<MultiSearchResponse> {
    public static final RawMultiSearchResponseExtractor INSTANCE = new RawMultiSearchResponseExtractor();

    private RawMultiSearchResponseExtractor() {
    }

    @Override
    public MultiSearchResponse doExtractData(MultiSearchResponse response) {
        return response;
    }
}

package com.ymm.ebatis.response;

import org.elasticsearch.action.search.SearchResponse;

/**
 * @author 章多亮
 * @since 2019/12/26 17:20
 */
public class RawSearchResponseExtractor extends SimpleResponseExtractor<SearchResponse> implements SearchResponseExtractor<SearchResponse> {
    public static final RawSearchResponseExtractor INSTANCE = new RawSearchResponseExtractor();

    private RawSearchResponseExtractor() {
    }
}

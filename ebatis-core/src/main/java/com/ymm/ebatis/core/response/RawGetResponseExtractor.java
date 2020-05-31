package com.ymm.ebatis.core.response;

import org.elasticsearch.action.get.GetResponse;

class RawGetResponseExtractor extends SimpleResponseExtractor<GetResponse> {
    static final RawGetResponseExtractor INSTANCE = new RawGetResponseExtractor();

    private RawGetResponseExtractor() {
    }
}

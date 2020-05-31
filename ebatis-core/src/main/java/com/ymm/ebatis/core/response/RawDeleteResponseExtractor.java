package com.ymm.ebatis.core.response;

import org.elasticsearch.action.delete.DeleteResponse;

public class RawDeleteResponseExtractor extends SimpleResponseExtractor<DeleteResponse> implements DeleteResponseExtractor<DeleteResponse> {
    public static final RawDeleteResponseExtractor INSTANCE = new RawDeleteResponseExtractor();

    private RawDeleteResponseExtractor() {
    }
}

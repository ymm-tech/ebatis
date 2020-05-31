package com.ymm.ebatis.core.response;

import org.elasticsearch.action.index.IndexResponse;

/**
 * @author 章多亮
 * @since 2019/12/26 17:10
 */
public class RawIndexResponseExtractor extends SimpleResponseExtractor<IndexResponse> implements IndexResponseExtractor<IndexResponse> {
    public static final RawIndexResponseExtractor INSTANCE = new RawIndexResponseExtractor();

    private RawIndexResponseExtractor() {
    }
}

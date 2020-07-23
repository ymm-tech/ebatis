package com.ymm.ebatis.core.response;

import org.elasticsearch.action.index.IndexResponse;

/**
 * @author weilong.hu
 * @since 2020/6/28 19:56
 */
public class StringIndexResponseExtractor implements IndexResponseExtractor<String> {
    public static final StringIndexResponseExtractor INSTANCE = new StringIndexResponseExtractor();

    private StringIndexResponseExtractor() {
    }

    @Override
    public String doExtractData(IndexResponse response) {
        return response.getId();
    }
}

package com.ymm.ebatis.core.response;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author weilong.hu
 * @since 2020/6/28 20:23
 */
public class RestStatusIndexResponseExtractor implements IndexResponseExtractor<RestStatus> {
    public static final RestStatusIndexResponseExtractor INSTANCE = new RestStatusIndexResponseExtractor();

    private RestStatusIndexResponseExtractor() {
    }

    @Override
    public RestStatus doExtractData(IndexResponse response) {
        return response.status();
    }
}

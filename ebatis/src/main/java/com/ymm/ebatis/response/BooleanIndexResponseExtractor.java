package com.ymm.ebatis.response;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author 章多亮
 * @since 2019/12/26 16:29
 */
public class BooleanIndexResponseExtractor implements IndexResponseExtractor<Boolean> {
    public static final BooleanIndexResponseExtractor INSTANCE = new BooleanIndexResponseExtractor();

    private BooleanIndexResponseExtractor() {
    }

    @Override
    public Boolean doExtractData(IndexResponse response) {
        return response.status() == RestStatus.CREATED;
    }
}

package com.ymm.ebatis.core.response;

import org.elasticsearch.action.bulk.BulkResponse;

/**
 * @author 章多亮
 * @since 2019/12/26 20:30
 */
public class RawBulkResponseExtractor implements BulkResponseExtractor<BulkResponse> {
    public static final RawBulkResponseExtractor INSTANCE = new RawBulkResponseExtractor();

    private RawBulkResponseExtractor() {
    }

    @Override
    public BulkResponse doExtractData(BulkResponse response) {
        return response;
    }
}

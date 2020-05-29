package com.ymm.ebatis.response;

import org.elasticsearch.action.bulk.BulkResponse;

/**
 * @author 章多亮
 * @since 2019/12/26 20:29
 */
public class BooleanBulkResponseExtractor implements BulkResponseExtractor<Boolean> {
    public static final BooleanBulkResponseExtractor INSTANCE = new BooleanBulkResponseExtractor();

    private BooleanBulkResponseExtractor() {
    }

    @Override
    public Boolean doExtractData(BulkResponse response) {
        return !response.hasFailures();
    }
}

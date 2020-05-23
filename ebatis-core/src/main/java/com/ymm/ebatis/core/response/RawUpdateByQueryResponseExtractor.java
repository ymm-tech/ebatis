package com.ymm.ebatis.core.response;

import org.elasticsearch.index.reindex.BulkByScrollResponse;

/**
 * @author 章多亮
 * @since 2019/12/30 13:53
 */
public class RawUpdateByQueryResponseExtractor extends SimpleResponseExtractor<BulkByScrollResponse> implements UpdateByQueryResponseExtractor<BulkByScrollResponse> {
    public static final RawUpdateByQueryResponseExtractor INSTANCE = new RawUpdateByQueryResponseExtractor();

    private RawUpdateByQueryResponseExtractor() {
    }
}

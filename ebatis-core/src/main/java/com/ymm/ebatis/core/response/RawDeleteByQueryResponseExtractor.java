package com.ymm.ebatis.core.response;

import org.elasticsearch.index.reindex.BulkByScrollResponse;

/**
 * @author duoliang.zhang
 */
public class RawDeleteByQueryResponseExtractor extends SimpleResponseExtractor<BulkByScrollResponse> implements DeleteByQueryResponseExtractor<BulkByScrollResponse> {
    public static final RawDeleteByQueryResponseExtractor INSTANCE = new RawDeleteByQueryResponseExtractor();

    private RawDeleteByQueryResponseExtractor() {
    }
}

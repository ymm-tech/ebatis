package com.ymm.ebatis.core.response;

import org.elasticsearch.index.reindex.BulkByScrollResponse;

/**
 * @author 章多亮
 * @since 2019/12/30 13:53
 */
public interface UpdateByQueryResponseExtractor<T> extends ConcreteResponseExtractor<T, BulkByScrollResponse> {
}

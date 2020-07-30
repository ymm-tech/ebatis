package io.manbang.ebatis.core.response;

import org.elasticsearch.action.bulk.BulkResponse;

/**
 * @author 章多亮
 * @since 2019/12/26 20:25
 */
public interface BulkResponseExtractor<T> extends ConcreteResponseExtractor<T, BulkResponse> {
}

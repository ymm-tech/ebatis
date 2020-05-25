package com.ymm.ebatis.response;

import org.elasticsearch.action.update.UpdateResponse;

/**
 * @author 章多亮
 * @since 2019/12/30 11:52
 */
public interface UpdateResponseExtractor<T> extends ConcreteResponseExtractor<T, UpdateResponse> {
}

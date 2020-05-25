package com.ymm.ebatis.response;

import org.elasticsearch.action.index.IndexResponse;

/**
 * @author 章多亮
 * @since 2019/12/19 14:13
 */
public interface IndexResponseExtractor<T> extends ConcreteResponseExtractor<T, IndexResponse> {
}

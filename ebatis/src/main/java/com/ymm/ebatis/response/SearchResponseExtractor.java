package com.ymm.ebatis.response;

import org.elasticsearch.action.search.SearchResponse;

/**
 * 搜索响应抽提器
 *
 * @author duoliang.zhang
 */
public interface SearchResponseExtractor<T> extends ConcreteResponseExtractor<T, SearchResponse> {
}


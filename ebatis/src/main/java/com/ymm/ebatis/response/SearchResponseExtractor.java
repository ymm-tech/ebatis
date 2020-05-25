package com.ymm.ebatis.response;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchResponse;

/**
 * 搜索响应抽提器
 *
 * @author duoliang.zhang
 */
public interface SearchResponseExtractor<T> extends ConcreteResponseExtractor<T, SearchResponse> {
    @Override
    default boolean validate(ActionResponse response) {
        return true;
    }
}


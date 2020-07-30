package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;

/**
 * @author 章多亮
 * @since 2020/1/14 16:30
 */
public interface MultiSearchResponseExtractor<T> extends ConcreteResponseExtractor<T, MultiSearchResponse> {
}

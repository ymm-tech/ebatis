package com.ymm.ebatis.response;

import org.elasticsearch.action.search.MultiSearchResponse;

/**
 * @author 章多亮
 * @since 2020/1/14 17:56
 */
public class ArrayMultiSearchResponseExtractor<T> implements MultiSearchResponseExtractor<T[][]> {

    private final DocumentExtractor<T> extractor;

    public ArrayMultiSearchResponseExtractor(DocumentExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public T[][] doExtractData(MultiSearchResponse response) {
        return empty();
    }
}

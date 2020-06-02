package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/14 17:56
 */
public class ArrayMultiSearchResponseExtractor<T> implements MultiSearchResponseExtractor<T[][]> {

    private final ArrayDocumentExtractor<T> extractor;

    public ArrayMultiSearchResponseExtractor(ArrayDocumentExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[][] doExtractData(MultiSearchResponse response) {
        Object[][] result = Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(extractor::doExtractData)
                .toArray(Object[][]::new);

        return (T[][]) result;
    }
}

package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;

/**
 * @author duoliang.zhang
 */
public class NoDocumentExtractor<T> implements SearchResponseExtractor<T> {
    public static final NoDocumentExtractor<?> INSTANCE = new NoDocumentExtractor<>();

    private NoDocumentExtractor() {
    }

    @Override
    public T doExtractData(SearchResponse response) {
        return null;
    }
}

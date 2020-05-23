package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;

import java.util.Iterator;

/**
 * @author duoliang.zhang
 */
public class SingleDocumentExtractor<T> implements SearchResponseExtractor<T> {
    private final DocumentExtractor<T> documentExtractor;

    public SingleDocumentExtractor(DocumentMapper<T> documentMapper) {
        this.documentExtractor = new DocumentExtractor<>(documentMapper, 1);
    }

    @Override
    public T doExtractData(SearchResponse response) {
        Iterator<T> iterator = documentExtractor.extractData(response).iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
}

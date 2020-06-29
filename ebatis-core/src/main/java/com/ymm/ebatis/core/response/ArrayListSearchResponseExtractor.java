package com.ymm.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2020/6/29 15:27
 */
public class ArrayListSearchResponseExtractor<T> implements MultiSearchResponseExtractor<List<T>[]> {
    private final DocumentExtractor<T> documentExtractor;

    public ArrayListSearchResponseExtractor(DocumentExtractor<T> documentExtractor) {
        this.documentExtractor = documentExtractor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T>[] doExtractData(MultiSearchResponse response) {
        MultiSearchResponse.Item[] responses = response.getResponses();
        List<T>[] lists = new ArrayList[responses.length];
        for (int i = 0; i < responses.length; i++) {
            MultiSearchResponse.Item item = responses[i];
            List<T> list = documentExtractor.doExtractData(item.getResponse());
            lists[i] = list;
        }
        return lists;
    }
}

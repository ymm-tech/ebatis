package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * @author weilong.hu
 * @since 2020/6/28 10:28
 */
public class ListArraySearchResponseExtractor<T> implements MultiSearchResponseExtractor<List<T[]>> {
    private final ArrayDocumentExtractor<T> extractor;

    public ListArraySearchResponseExtractor(ArrayDocumentExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public List<T[]> doExtractData(MultiSearchResponse response) {
        MultiSearchResponse.Item[] responses = response.getResponses();
        List<T[]> list = new LinkedList<>();
        for (MultiSearchResponse.Item item : responses) {
            list.add(extractor.doExtractData(item.getResponse()));
        }
        return list;
    }
}

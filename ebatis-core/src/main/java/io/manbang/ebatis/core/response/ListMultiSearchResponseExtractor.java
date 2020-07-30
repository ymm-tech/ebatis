package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/1/14 17:15
 */
public class ListMultiSearchResponseExtractor<T> implements MultiSearchResponseExtractor<List<List<T>>> {
    private final DocumentExtractor<T> documentExtractor;

    public ListMultiSearchResponseExtractor(DocumentExtractor<T> documentExtractor) {
        this.documentExtractor = documentExtractor;
    }

    @Override
    public List<List<T>> doExtractData(MultiSearchResponse response) {
        return Stream.of(response.getResponses())
                .map(MultiSearchResponse.Item::getResponse)
                .map(documentExtractor::doExtractData)
                .collect(Collectors.toList());
    }
}

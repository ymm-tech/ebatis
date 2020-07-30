package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.domain.Page;
import io.manbang.ebatis.core.domain.ScrollResponse;
import org.elasticsearch.action.search.SearchResponse;

/**
 * @author 章多亮
 * @since 2020/6/8 14:09
 */
public class ScrollResponseExtractor<T> implements ConcreteResponseExtractor<ScrollResponse<T>, SearchResponse> {
    private final DocumentPageExtractor<T> documentPageExtractor;

    public ScrollResponseExtractor(DocumentPageExtractor<T> documentPageExtractor) {
        this.documentPageExtractor = documentPageExtractor;
    }

    @Override
    public ScrollResponse<T> doExtractData(SearchResponse response) {
        Page<T> page = documentPageExtractor.doExtractData(response);
        return ScrollResponse.of(response.getScrollId(), page);
    }
}

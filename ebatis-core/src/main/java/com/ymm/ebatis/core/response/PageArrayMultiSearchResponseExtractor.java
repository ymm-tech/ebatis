package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.PageImpl;
import com.ymm.ebatis.core.domain.Pageable;
import org.elasticsearch.action.search.MultiSearchResponse;

/**
 * @author weilong.hu
 * @since 2020/6/24 14:23
 */
public class PageArrayMultiSearchResponseExtractor<T> implements MultiSearchResponseExtractor<Page<T>[]> {
    private final DocumentPageExtractor<T> extractor;

    public PageArrayMultiSearchResponseExtractor(DocumentPageExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public Page<T>[] doExtractData(MultiSearchResponse response) {
        MultiSearchResponse.Item[] responses = response.getResponses();

        Pageable[] pageables = ContextHolder.getContext().getPageables().orElse(new Pageable[response.getResponses().length]);
        @SuppressWarnings("unchecked")
        Page<T>[] pages = new PageImpl[pageables.length];
        for (int i = 0; i < responses.length; i++) {
            MultiSearchResponse.Item item = responses[i];
            ContextHolder.setPageable(pageables[i]);
            pages[i] = extractor.doExtractData(item.getResponse());
        }

        return pages;
    }
}

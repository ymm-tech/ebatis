package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import org.elasticsearch.action.search.MultiSearchResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/15 10:50
 */
public class PageMultiSearchResponseExtractor<T> implements MultiSearchResponseExtractor<List<Page<T>>> {
    private final DocumentPageExtractor<T> extractor;

    public PageMultiSearchResponseExtractor(DocumentPageExtractor<T> extractor) {
        this.extractor = extractor;
    }

    @Override
    public List<Page<T>> doExtractData(MultiSearchResponse response) {
        List<Page<T>> pages = new LinkedList<>();
        MultiSearchResponse.Item[] responses = response.getResponses();

        Pageable[] pageables = ContextHolder.getContext().getPageables().orElse(new Pageable[response.getResponses().length]);

        for (int i = 0; i < responses.length; i++) {
            MultiSearchResponse.Item item = responses[i];
            ContextHolder.setPageable(pageables[i]);
            pages.add(extractor.doExtractData(item.getResponse()));
        }

        return pages;
    }
}

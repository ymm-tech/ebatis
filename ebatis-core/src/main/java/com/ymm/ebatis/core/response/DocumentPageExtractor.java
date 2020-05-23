package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.exception.PageableNotFoundException;
import org.elasticsearch.action.search.SearchResponse;

import java.util.Collections;
import java.util.List;

/**
 * 分页文档提取器
 *
 * @author duoliang.zhang
 */
public class DocumentPageExtractor<T> implements SearchResponseExtractor<Page<T>> {
    private static final Page<?> EMPTY_PAGE = Page.of(0, Collections.emptyList(), Pageable.of(0, 20));
    private final DocumentExtractor<T> documentExtractor;

    public DocumentPageExtractor(DocumentMapper<T> documentMapper) {
        documentExtractor = new DocumentExtractor<>(documentMapper, Integer.MAX_VALUE);
    }

    @Override
    public Page<T> doExtractData(SearchResponse response) {
        List<T> documents = documentExtractor.extractData(response);
        long total = response.getHits().getTotalHits().value;
        Pageable pageable = ContextHolder.getContext().getPageable().orElseThrow(PageableNotFoundException::new);
        return Page.of(total, documents, pageable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> empty() {
        return (Page<T>) EMPTY_PAGE;
    }
}

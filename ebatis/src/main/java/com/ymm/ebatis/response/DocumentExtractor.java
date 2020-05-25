package com.ymm.ebatis.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author duoliang.zhang
 */
public class DocumentExtractor<T> implements SearchResponseExtractor<List<T>> {
    private final DocumentMapper<T> documentMapper;
    private final int documentsExpected;

    public DocumentExtractor(DocumentMapper<T> documentMapper, int documentsExpected) {
        this.documentMapper = documentMapper;
        this.documentsExpected = documentsExpected;
    }

    @Override
    public List<T> doExtractData(SearchResponse response) {
        // 返回的文档数目
        SearchHit[] hits = response.getHits().getHits();
        int hitCount = hits.length;
        // 和期待返回的数量比较，提取最小的
        int count = Math.min(documentsExpected, hitCount);

        List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(documentMapper.mapRow(hits[i], i + 1));
        }

        return result;
    }

    @Override
    public List<T> empty() {
        return Collections.emptyList();
    }
}

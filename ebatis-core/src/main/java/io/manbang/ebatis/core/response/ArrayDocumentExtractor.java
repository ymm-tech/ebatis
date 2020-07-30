package io.manbang.ebatis.core.response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.lang.reflect.Array;

/**
 * @author 章多亮
 * @since 2020/6/2 11:42
 */
public class ArrayDocumentExtractor<T> implements SearchResponseExtractor<T[]> {
    private final DocumentMapper<T> documentMapper;
    private final int expectedCount;

    public ArrayDocumentExtractor(DocumentMapper<T> documentMapper, int expectedCount) {
        this.documentMapper = documentMapper;
        this.expectedCount = expectedCount;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] doExtractData(SearchResponse response) {
        // 返回的文档数目
        SearchHit[] hits = response.getHits().getHits();
        int hitCount = hits.length;
        // 和期待返回的数量比较，提取最小的
        int count = Math.min(expectedCount, hitCount);
        T[] result = (T[]) Array.newInstance(documentMapper.getEntityClass(), count);

        for (int i = 0; i < count; i++) {
            result[i] = documentMapper.mapRow(hits[i], i + 1);
        }

        return result;
    }
}

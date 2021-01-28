package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.SearchHit;

/**
 * 元信息
 *
 * @author duoliang.zhang
 */
public interface MetaSource {
    /**
     * 创建元信息
     *
     * @param hit 命中文档
     * @return 元信息
     */
    static ResponseMeta of(SearchHit hit) {
        return new SimpleResponseMeta()
                .setId(hit.getId())
                .setIndex(hit.getIndex())
                .setType(hit.getType())
                .setScore(hit.getScore())
                .setVersion(hit.getVersion())
                .setSourceAsString(hit.getSourceAsString())
                .setSourceAsMap(hit.getSourceAsMap())
                .setSortValues(hit.getSortValues())
                .setClusterAlias(hit.getClusterAlias())
                .setMatchedQueries(hit.getMatchedQueries());
    }

    /**
     * 设置元信息
     *
     * @param responseMeta 元信息
     */
    void setResponseMeta(ResponseMeta responseMeta);
}

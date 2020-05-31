package com.ymm.ebatis.core.domain;

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
                .setType(hit.getType());
    }

    /**
     * 设置元信息
     *
     * @param responseMeta 元信息
     */
    void setResponseMeta(ResponseMeta responseMeta);
}

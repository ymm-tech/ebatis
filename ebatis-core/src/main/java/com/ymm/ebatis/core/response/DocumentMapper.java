package com.ymm.ebatis.core.response;

import org.elasticsearch.search.SearchHit;

/**
 * @author duoliang.zhang
 */
public interface DocumentMapper<T> {

    /**
     * 通过实体类型创建文档映射器
     *
     * @param entityClass 实体对象
     * @param <T>         实体对象类型
     * @return 文档映射器
     */
    static <T> DocumentMapper<T> of(Class<T> entityClass) {
        return FastJsonDocumentMapper.of(entityClass);
    }

    /**
     * 将ES文档映射成实体对象
     *
     * @param hit   命中的文档
     * @param index 文档下标
     * @return 映射后的实体对象
     */
    T mapRow(SearchHit hit, int index);
}

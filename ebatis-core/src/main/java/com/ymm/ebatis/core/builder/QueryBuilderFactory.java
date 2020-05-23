package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author duoliang.zhang
 */
public interface QueryBuilderFactory {
    /**
     * 创建查询构建器
     *
     * @param conditionMeta 查询条件
     * @param condition     条件
     * @return 查询构建器
     */
    QueryBuilder create(ConditionMeta<?> conditionMeta, Object condition);
}

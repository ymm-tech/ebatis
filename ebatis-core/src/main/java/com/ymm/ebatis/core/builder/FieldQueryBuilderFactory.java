package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Field;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Field查询
 *
 * @author duoliang.zhang
 */
class FieldQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Field> {
    static final FieldQueryBuilderFactory INSTANCE = new FieldQueryBuilderFactory();

    private FieldQueryBuilderFactory() {
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta conditionMeta, Object condition) {
        return null;
    }
}

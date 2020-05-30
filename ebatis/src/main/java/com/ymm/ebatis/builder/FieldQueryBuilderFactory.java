package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Field;
import com.ymm.ebatis.meta.ConditionMeta;
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

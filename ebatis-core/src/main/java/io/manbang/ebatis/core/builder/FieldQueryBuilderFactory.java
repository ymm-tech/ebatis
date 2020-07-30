package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Field;
import io.manbang.ebatis.core.meta.ConditionMeta;
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

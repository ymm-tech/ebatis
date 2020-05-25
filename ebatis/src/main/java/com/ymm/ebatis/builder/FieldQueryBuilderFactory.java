package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Field;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Field查询
 *
 * @author duoliang.zhang
 */
public class FieldQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Field> {

    @Override
    protected QueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return null;
    }
}

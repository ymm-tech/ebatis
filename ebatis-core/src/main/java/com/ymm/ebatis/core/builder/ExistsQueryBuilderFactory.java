package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Exists;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/1/7 15:45
 */
class ExistsQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Exists> {
    static final ExistsQueryBuilderFactory INSTANCE = new ExistsQueryBuilderFactory();

    private ExistsQueryBuilderFactory() {
    }

    @Override
    protected boolean onlyHandleNoneNullable() {
        return false;
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        Optional<Exists> exists = meta.findAttributeAnnotation(Exists.class);
        ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(meta.getName());
        if (exists.isPresent()) {
            return exists.get().value() ? existsQuery : QueryBuilders.boolQuery().mustNot(existsQuery);
        } else {
            return existsQuery;
        }
    }
}

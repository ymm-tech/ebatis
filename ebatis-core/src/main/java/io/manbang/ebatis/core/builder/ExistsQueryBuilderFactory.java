package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Exists;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 15:45
 */
class ExistsQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Exists> {
    static final ExistsQueryBuilderFactory INSTANCE = new ExistsQueryBuilderFactory();

    private ExistsQueryBuilderFactory() {
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        Boolean bool = (Boolean) condition;
        ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(meta.getName());
        if (bool) {
            return existsQuery;
        } else {
            return QueryBuilders.boolQuery().mustNot(existsQuery);
        }
    }
}

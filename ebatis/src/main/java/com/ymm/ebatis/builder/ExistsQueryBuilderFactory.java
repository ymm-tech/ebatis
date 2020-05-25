package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Exists;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/1/7 15:45
 */
public class ExistsQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Exists> {
    public static final ExistsQueryBuilderFactory INSTANCE = new ExistsQueryBuilderFactory();

    private ExistsQueryBuilderFactory() {
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        Optional<Exists> annotation = conditionMeta.getAttributeAnnotation(Exists.class);
        ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(conditionMeta.getName());
        if (annotation.isPresent()) {
            return annotation.get().value() ? existsQuery : QueryBuilders.boolQuery().mustNot(existsQuery);
        } else {
            return existsQuery;
        }
    }
}

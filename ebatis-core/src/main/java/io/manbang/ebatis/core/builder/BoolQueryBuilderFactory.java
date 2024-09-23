package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Bool;
import io.manbang.ebatis.core.meta.ClassMeta;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.meta.FieldMeta;
import io.manbang.ebatis.core.meta.NestNameHolder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;


/**
 * @author duoliang.zhang
 */
@Slf4j
class BoolQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Bool> {
    static final BoolQueryBuilderFactory INSTANCE = new BoolQueryBuilderFactory();

    private BoolQueryBuilderFactory() {
    }

    private Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(ConditionMeta meta, Object condition) {
        return meta == null ? ClassMeta.of(condition.getClass()).getQueryClauses() : meta.getQueryClauses(condition);
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        if (meta != null && meta.isNested()) {
            NestNameHolder.get().push(meta.getName());
        }

        try {
            val clauses = getQueryClauses(meta, condition);
            for (val clause : clauses.entrySet()) {
                val clauseType = QueryClauseType.valueOf(clause.getKey());
                clauseType.addQueryBuilder(builder, clause.getValue(), condition);
            }

            if (builder.hasClauses()) {
                val mustSize = builder.must().size();
                val mustNotSize = builder.mustNot().size();
                val shouldSize = builder.should().size();
                val filterSize = builder.filter().size();

                if (mustNotSize > 0 || shouldSize > 0 || filterSize > 0) {
                    return builder;
                }

                if (mustSize == 1) {
                    return builder.must().get(0);
                }
            }

            return null;
        } finally {
            if (meta != null && meta.isNested()) {
                NestNameHolder.get().pop();
            }
        }
    }
}


package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Must;
import io.manbang.ebatis.core.domain.Range;
import io.manbang.ebatis.core.domain.Script;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 11:03
 */
class AutoQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Must> {
    static final AutoQueryBuilderFactory INSTANCE = new AutoQueryBuilderFactory();

    private AutoQueryBuilderFactory() {
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        if (meta.isBasic()) {
            return QueryBuilderFactory.term().create(meta, condition);
        } else if (meta.isRange()) {
            Range<?> range = (Range<?>) condition;
            range.setName(meta.getName());
            return range.toBuilder();
        } else if (meta.isScript()) {
            Script script = (Script) condition;
            return QueryBuilders.scriptQuery(script.toEsScript());
        } else {
            return QueryBuilderFactory.bool().create(meta, condition);
        }
    }
}

package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Must;
import com.ymm.ebatis.domain.Range;
import com.ymm.ebatis.domain.Script;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 11:03
 */
public class AutoQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, Must> {
    public static final AutoQueryBuilderFactory INSTANCE = new AutoQueryBuilderFactory();

    private AutoQueryBuilderFactory() {
    }

    @Override
    public QueryBuilder create(ConditionMeta meta, Object condition) {
        if (meta.isBasicArrayOrCollection()) {
            return QueryBuilderFactory.terms().create(meta, condition);
        } else if (meta.isBasic()) {
            return QueryBuilderFactory.term().create(meta, condition);
        } else if (meta.isRange()) {
            Range<?, ?> range = (Range<?, ?>) condition;
            range.setName(meta.getName());
            return range.toBuilder();
        } else if (meta.isScript()) {
            Script script = (Script) condition;
            return QueryBuilders.scriptQuery(script.toEsScript());
        } else {
            return QueryBuilderFactory.bool().create(meta, condition);
        }
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return null;
    }
}

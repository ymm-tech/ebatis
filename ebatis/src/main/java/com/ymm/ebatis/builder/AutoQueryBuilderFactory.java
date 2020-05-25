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
    protected QueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        if (conditionMeta.isArrayOrCollection() && conditionMeta.isArrayOrCollectionBasicCondition()) {
            return TermsQueryBuilderFactory.INSTANCE.create(conditionMeta, condition);
        } else if (conditionMeta.isBasic()) {
            return TermQueryBuilderFactory.INSTANCE.create(conditionMeta, condition);
        } else if (conditionMeta.isRange()) {
            Range<?, ?> range = (Range<?, ?>) condition;
            range.setName(conditionMeta.getName());
            return range.toBuilder();
        } else if (conditionMeta.isScript()) {
            Script script = (Script) condition;
            return QueryBuilders.scriptQuery(script.toEsScript());
        } else {
            return BoolQueryBuilderFactory.INSTANCE.create(conditionMeta, condition);
        }
    }
}

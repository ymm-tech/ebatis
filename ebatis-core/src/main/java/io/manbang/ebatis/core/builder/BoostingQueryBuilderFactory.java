package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Boosting;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.BoostingProvider;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

class BoostingQueryBuilderFactory extends AbstractQueryBuilderFactory<BoostingQueryBuilder, Boosting> {
    static final BoostingQueryBuilderFactory INSTANCE = new BoostingQueryBuilderFactory();

    private BoostingQueryBuilderFactory() {
    }

    @Override
    protected BoostingQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        if (!(condition instanceof BoostingProvider)) {
            throw new ConditionNotSupportException("条件必须实现: BoostingProvider");
        }
        final BoostingProvider provider = (BoostingProvider) condition;
        final QueryBuilder positive = BoolQueryBuilderFactory.INSTANCE.create(meta, provider.positive());
        final QueryBuilder negative = BoolQueryBuilderFactory.INSTANCE.create(meta, provider.negative());
        return QueryBuilders.boostingQuery(positive, negative).negativeBoost(provider.negativeBoost());
    }
}

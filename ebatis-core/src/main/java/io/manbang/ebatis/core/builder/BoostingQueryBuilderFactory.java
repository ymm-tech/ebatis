package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Boosting;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.BoostingQueryBuilder;

class BoostingQueryBuilderFactory extends AbstractQueryBuilderFactory<BoostingQueryBuilder, Boosting> {
    static final BoostingQueryBuilderFactory INSTANCE = new BoostingQueryBuilderFactory();

    private BoostingQueryBuilderFactory() {
    }

    @Override
    protected BoostingQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return null;
    }
}

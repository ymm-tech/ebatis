package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Boosting;
import com.ymm.ebatis.core.meta.ConditionMeta;
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

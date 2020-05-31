package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Boosting;
import com.ymm.ebatis.meta.ConditionMeta;
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

package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.ConstantScore;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 常记分
 *
 * @author 章多亮
 */
public class ConstantScoreQueryBuilderFactory extends AbstractQueryBuilderFactory<ConstantScoreQueryBuilder, ConstantScore> {
    public static final ConstantScoreQueryBuilderFactory INSTANCE = new ConstantScoreQueryBuilderFactory();

    private ConstantScoreQueryBuilderFactory() {
    }

    @Override
    protected ConstantScoreQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        QueryBuilder builder = AutoQueryBuilderFactory.INSTANCE.create(meta, condition);
        return QueryBuilders.constantScoreQuery(builder);
    }
}

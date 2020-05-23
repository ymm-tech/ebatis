package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Term;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 * @author 章多亮
 * @since 2020/1/7 10:33
 */
public class TermQueryBuilderFactory extends AbstractQueryBuilderFactory<TermQueryBuilder, Term> {
    public static final TermQueryBuilderFactory INSTANCE = new TermQueryBuilderFactory();

    private TermQueryBuilderFactory() {
    }

    @Override
    protected TermQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return QueryBuilders.termQuery(conditionMeta.getName(), condition);
    }
}

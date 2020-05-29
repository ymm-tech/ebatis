package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.MatchAll;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 9:29
 */
public class MatchAllQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchAllQueryBuilder, MatchAll> {
    public static final MatchAllQueryBuilderFactory INSTANCE = new MatchAllQueryBuilderFactory();

    private MatchAllQueryBuilderFactory() {
    }

    @Override
    protected MatchAllQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.matchAllQuery();
    }
}

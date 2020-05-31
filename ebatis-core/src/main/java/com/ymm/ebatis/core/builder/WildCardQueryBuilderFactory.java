package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Wildcard;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;

/**
 * @author weilong.hu
 * @since 2020/5/22 13:39
 */
class WildCardQueryBuilderFactory extends AbstractQueryBuilderFactory<WildcardQueryBuilder, Wildcard> {
    static final WildCardQueryBuilderFactory INSTANCE = new WildCardQueryBuilderFactory();

    @Override
    protected void setAnnotationMeta(WildcardQueryBuilder builder, Wildcard wildcard) {
        builder.rewrite(wildcard.rewrite());
    }

    @Override
    protected WildcardQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.wildcardQuery(meta.getName(), String.valueOf(condition));
    }
}

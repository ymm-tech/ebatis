package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Wildcard;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;

/**
 * @author weilong.hu
 * @since 2020/5/22 13:39
 */
class WildcardQueryBuilderFactory extends AbstractQueryBuilderFactory<WildcardQueryBuilder, Wildcard> {
    static final WildcardQueryBuilderFactory INSTANCE = new WildcardQueryBuilderFactory();

    @Override
    protected void setAnnotationMeta(WildcardQueryBuilder builder, Wildcard wildcard) {
        builder.rewrite(StringUtils.trimToNull(wildcard.rewrite()));
    }

    @Override
    protected WildcardQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.wildcardQuery(meta.getName(), String.valueOf(condition));
    }
}

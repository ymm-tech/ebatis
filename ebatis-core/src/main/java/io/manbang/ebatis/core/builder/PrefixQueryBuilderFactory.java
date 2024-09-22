package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Prefix;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

class PrefixQueryBuilderFactory extends AbstractQueryBuilderFactory<PrefixQueryBuilder, Prefix> {
    static final PrefixQueryBuilderFactory INSTANCE = new PrefixQueryBuilderFactory();

    @Override
    protected void setAnnotationMeta(PrefixQueryBuilder builder, Prefix prefix) {
        builder.boost(prefix.boost())
                .caseInsensitive(prefix.caseInsensitive());

        if (!prefix.rewrite().isEmpty()) {
            builder.rewrite(prefix.rewrite());
        }
    }

    @Override
    protected PrefixQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.prefixQuery(meta.getName(), condition.toString());
    }

}

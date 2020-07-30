package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.MatchPhrasePrefix;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 10:27
 */
class MatchPhrasePrefixQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchPhrasePrefixQueryBuilder, MatchPhrasePrefix> {
    static final MatchPhrasePrefixQueryBuilderFactory INSTANCE = new MatchPhrasePrefixQueryBuilderFactory();

    private MatchPhrasePrefixQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MatchPhrasePrefixQueryBuilder builder, MatchPhrasePrefix matchPhrasePrefix) {
        builder.maxExpansions(matchPhrasePrefix.maxExpansions())
                .slop(matchPhrasePrefix.slop())
                .analyzer(StringUtils.trimToNull(matchPhrasePrefix.analyzer()));
    }

    @Override
    protected MatchPhrasePrefixQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.matchPhrasePrefixQuery(meta.getName(), condition);
    }
}

package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.MatchPhrase;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 10:20
 */
class MatchPhraseQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchPhraseQueryBuilder, MatchPhrase> {
    static final MatchPhraseQueryBuilderFactory INSTANCE = new MatchPhraseQueryBuilderFactory();

    private MatchPhraseQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MatchPhraseQueryBuilder builder, MatchPhrase matchPhrase) {
        builder.zeroTermsQuery(matchPhrase.zeroTermsQuery())
                .slop(matchPhrase.slop())
                .analyzer(StringUtils.trimToNull(matchPhrase.analyzer()));
    }

    @Override
    protected MatchPhraseQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.matchPhraseQuery(meta.getName(), condition);
    }
}

package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Match;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.ZeroTermsQueryOption;

/**
 * @author 章多亮
 * @since 2020/1/7 9:28
 */
class MatchQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchQueryBuilder, Match> {
    static final MatchQueryBuilderFactory INSTANCE = new MatchQueryBuilderFactory();

    private MatchQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MatchQueryBuilder builder, Match match) {
        builder.autoGenerateSynonymsPhraseQuery(match.autoGenerateSynonymsPhraseQuery())
                .operator(match.operator())
                .fuzzyTranspositions(match.fuzzyTranspositions())
                .lenient(match.lenient())
                .maxExpansions(match.maxExpansions())
                .fuzzyRewrite(StringUtils.trimToNull(match.fuzzyRewrite()))
                .minimumShouldMatch(StringUtils.trimToNull(match.minimumShouldMatch()))
                .fuzziness(StringUtils.trimToNull(match.fuzziness()))
                .analyzer(StringUtils.trimToNull(match.analyzer()))
                .prefixLength(match.prefixLength());

        if (!match.zeroTermsQuery().isEmpty()) {
            builder.zeroTermsQuery(ZeroTermsQueryOption.valueOf(match.zeroTermsQuery().toUpperCase()));
        }
    }

    @Override
    protected MatchQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.matchQuery(meta.getName(), condition);
    }
}

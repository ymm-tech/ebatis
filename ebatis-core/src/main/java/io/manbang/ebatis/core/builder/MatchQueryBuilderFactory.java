package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Match;
import io.manbang.ebatis.core.builder.compatibility.CompatibleMatchQueryBuilder;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 章多亮
 * @since 2020/1/7 9:28
 */
class MatchQueryBuilderFactory extends AbstractQueryBuilderFactory<CompatibleMatchQueryBuilder, Match> {
    static final MatchQueryBuilderFactory INSTANCE = new MatchQueryBuilderFactory();

    private MatchQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(CompatibleMatchQueryBuilder builder, Match match) {
        builder.operator(match.operator())
                .fuzzyTranspositions(match.fuzzyTranspositions())
                .lenient(match.lenient())
                .maxExpansions(match.maxExpansions())
                .zeroTermsQuery(match.zeroTermsQuery())
                .fuzzyRewrite(StringUtils.trimToNull(match.fuzzyRewrite()))
                .minimumShouldMatch(StringUtils.trimToNull(match.minimumShouldMatch()))
                .fuzziness(StringUtils.trimToNull(match.fuzziness()))
                .analyzer(StringUtils.trimToNull(match.analyzer()))
                .prefixLength(match.prefixLength());

        if (match.cutoffFrequency() >= 0 && match.cutoffFrequency() <= 1) {
            builder.cutoffFrequency(match.cutoffFrequency());
        }
    }

    @Override
    protected CompatibleMatchQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return new CompatibleMatchQueryBuilder(meta.getName(), condition);
    }
}

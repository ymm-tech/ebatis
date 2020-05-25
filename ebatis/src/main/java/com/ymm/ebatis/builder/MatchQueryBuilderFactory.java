package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Match;
import com.ymm.ebatis.meta.ConditionMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/7 9:28
 */
public class MatchQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchQueryBuilder, Match> {
    public static final MatchQueryBuilderFactory INSTANCE = new MatchQueryBuilderFactory();

    private MatchQueryBuilderFactory() {
    }

    @Override
    protected void setOptionalMeta(MatchQueryBuilder builder, Match match) {
        builder.autoGenerateSynonymsPhraseQuery(match.autoGenerateSynonymsPhraseQuery())
                .operator(match.operator())
                .fuzzyTranspositions(match.fuzzyTranspositions())
                .lenient(match.lenient())
                .maxExpansions(match.maxExpansions())
                .zeroTermsQuery(match.zeroTermsQuery())
                .prefixLength(match.prefixLength());

        if (match.cutoffFrequency() >= 0 && match.cutoffFrequency() <= 1) {
            builder.cutoffFrequency(match.cutoffFrequency());
        }

        if (StringUtils.isNotBlank(match.analyzer())) {
            builder.analyzer(match.analyzer());
        }

        if (StringUtils.isNotBlank(match.fuzziness())) {
            builder.fuzziness(match.fuzziness());
        }

        if (StringUtils.isNotBlank(match.fuzzyRewrite())) {
            builder.fuzzyRewrite(match.fuzzyRewrite());
        }

        if (StringUtils.isNotBlank(match.minimumShouldMatch())) {
            builder.minimumShouldMatch(match.minimumShouldMatch());
        }
    }

    @Override
    protected MatchQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return QueryBuilders.matchQuery(conditionMeta.getName(), condition);
    }
}

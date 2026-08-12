package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.MatchBoolPrefix;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.MatchBoolPrefixQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;

class MatchBoolPrefixQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchBoolPrefixQueryBuilder, MatchBoolPrefix> {
    static final MatchBoolPrefixQueryBuilderFactory INSTANCE = new MatchBoolPrefixQueryBuilderFactory();

    private MatchBoolPrefixQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MatchBoolPrefixQueryBuilder builder, MatchBoolPrefix matchBoolPrefix) {
        builder.boost(matchBoolPrefix.boost())
                .operator(Operator.fromString(matchBoolPrefix.operator()))
                .fuzzyTranspositions(matchBoolPrefix.fuzzyTranspositions())
                .prefixLength(matchBoolPrefix.prefixLength())
                .maxExpansions(matchBoolPrefix.maxExpansions());

        if (!matchBoolPrefix.fuzziness().isEmpty()) {
            builder.fuzziness(matchBoolPrefix.fuzziness());
        }

        if (!matchBoolPrefix.minimumShouldMatch().isEmpty()) {
            builder.minimumShouldMatch(matchBoolPrefix.minimumShouldMatch());
        }

        if (!matchBoolPrefix.fuzzyRewrite().isEmpty()) {
            builder.fuzzyRewrite(matchBoolPrefix.fuzzyRewrite());
        }

        if (!matchBoolPrefix.analyzer().isEmpty()) {
            builder.analyzer(matchBoolPrefix.analyzer());
        }

    }

    @Override
    protected MatchBoolPrefixQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.matchBoolPrefixQuery(meta.getName(), condition);
    }
}

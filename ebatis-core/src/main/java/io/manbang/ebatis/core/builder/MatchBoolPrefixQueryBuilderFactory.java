package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.MatchBoolPrefix;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.MatchBoolPrefixQueryBuilder;
import org.elasticsearch.index.query.Operator;

class MatchBoolPrefixQueryBuilderFactory extends AbstractQueryBuilderFactory<MatchBoolPrefixQueryBuilder, MatchBoolPrefix> {
    static final MatchBoolPrefixQueryBuilderFactory INSTANCE = new MatchBoolPrefixQueryBuilderFactory();

    private MatchBoolPrefixQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MatchBoolPrefixQueryBuilder builder, MatchBoolPrefix annotation) {
        builder.operator(Operator.fromString(annotation.operator()))
                .fuzziness(annotation.fuzziness())
                .fuzzyTranspositions(annotation.fuzzyTranspositions())
                .prefixLength(annotation.prefixLength())
                .maxExpansions(annotation.maxExpansions());

        if (!annotation.fuzziness().isEmpty()) {
            builder.fuzziness(annotation.fuzziness());
        }

        if (!annotation.minimumShouldMatch().isEmpty()) {
            builder.minimumShouldMatch(annotation.minimumShouldMatch());
        }

        if (!annotation.fuzzyRewrite().isEmpty()) {
            builder.fuzzyRewrite(annotation.fuzzyRewrite());
        }

        if (!annotation.analyzer().isEmpty()) {
            builder.analyzer(annotation.analyzer());
        }

    }

    @Override
    protected MatchBoolPrefixQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return new MatchBoolPrefixQueryBuilder(meta.getName(), condition.toString());
    }
}

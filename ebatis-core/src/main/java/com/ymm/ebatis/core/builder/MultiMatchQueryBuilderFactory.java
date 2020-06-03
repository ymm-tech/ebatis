package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.MultiMatch;
import com.ymm.ebatis.core.exception.EbatisException;
import com.ymm.ebatis.core.meta.ConditionMeta;
import com.ymm.ebatis.core.provider.MultiMatchFieldProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/16 18:59
 */
class MultiMatchQueryBuilderFactory extends AbstractQueryBuilderFactory<MultiMatchQueryBuilder, MultiMatch> {
    static final MultiMatchQueryBuilderFactory INSTANCE = new MultiMatchQueryBuilderFactory();

    private MultiMatchQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(MultiMatchQueryBuilder builder, MultiMatch multiMatch) {
        Float cutoffFrequency = multiMatch.cutoffFrequency() < 0 || multiMatch.cutoffFrequency() > 1 ? null : multiMatch.cutoffFrequency();

        builder.autoGenerateSynonymsPhraseQuery(multiMatch.autoGenerateSynonymsPhraseQuery())
                .cutoffFrequency(cutoffFrequency)
                .fuzziness(StringUtils.trimToNull(multiMatch.fuzziness()))
                .fuzzyRewrite(StringUtils.trimToNull(multiMatch.fuzzyRewrite()))
                .fuzzyTranspositions(multiMatch.fuzzyTranspositions())
                .lenient(multiMatch.lenient())
                .maxExpansions(multiMatch.maxExpansions())
                .prefixLength(multiMatch.prefixLength())
                .minimumShouldMatch(StringUtils.trimToNull(multiMatch.minimumShouldMatch()))
                .operator(multiMatch.operator())
                .slop(multiMatch.slop())
                .tieBreaker(multiMatch.tieBreaker());
    }

    @Override
    protected MultiMatchQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        MultiMatch multiMatch = meta.findAttributeAnnotation(MultiMatch.class).orElseThrow(EbatisException::new);

        String[] fields = multiMatch.fields();

        if (condition instanceof MultiMatchFieldProvider) {
            fields = ArrayUtils.addAll(fields, ((MultiMatchFieldProvider) condition).getFields());
        }

        return QueryBuilders.multiMatchQuery(String.valueOf(condition), fields);
    }
}

package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.MultiMatch;
import io.manbang.ebatis.core.exception.ConditionNotSupportException;
import io.manbang.ebatis.core.meta.ConditionMeta;
import io.manbang.ebatis.core.provider.MultiMatchFieldProvider;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.ZeroTermsQueryOption;

import java.util.Objects;

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
        builder.autoGenerateSynonymsPhraseQuery(multiMatch.autoGenerateSynonymsPhraseQuery())
                .fuzziness(StringUtils.trimToNull(multiMatch.fuzziness()))
                .fuzzyRewrite(StringUtils.trimToNull(multiMatch.fuzzyRewrite()))
                .fuzzyTranspositions(multiMatch.fuzzyTranspositions())
                .type(MultiMatchQueryBuilder.Type.valueOf(multiMatch.type().toUpperCase()))
                .zeroTermsQuery(ZeroTermsQueryOption.valueOf(multiMatch.zeroTermsQuery().toUpperCase()))
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
        if (!(condition instanceof MultiMatchFieldProvider)) {
            throw new ConditionNotSupportException("条件必须实现: MultiMatchFieldProvider");
        }
        String[] fields = ((MultiMatchFieldProvider) condition).getFields();

        final MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(((MultiMatchFieldProvider) condition).text());
        if (Objects.nonNull(fields)) {
            for (String field : fields) {
                final String[] split = StringUtils.split(field, "^");
                if (split.length == 1) {
                    multiMatchQueryBuilder.field(split[0]);
                } else {
                    multiMatchQueryBuilder.field(split[0], Float.parseFloat(split[1]));
                }

            }
        }
        return multiMatchQueryBuilder;
    }
}

package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.Fuzzy;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/8 17:30
 */
class FuzzyQueryBuilderFactory extends AbstractQueryBuilderFactory<FuzzyQueryBuilder, Fuzzy> {
    static final FuzzyQueryBuilderFactory INSTANCE = new FuzzyQueryBuilderFactory();

    private FuzzyQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(FuzzyQueryBuilder builder, Fuzzy fuzzy) {
        builder.fuzziness(Fuzziness.build(fuzzy.fuzziness()))
                .maxExpansions(fuzzy.maxExpansions())
                .prefixLength(fuzzy.prefixLength());
    }

    @Override
    protected FuzzyQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        return QueryBuilders.fuzzyQuery(meta.getName(), condition);
    }
}

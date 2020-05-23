package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.Fuzzy;
import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/8 17:30
 */
public class FuzzyQueryBuilderFactory extends AbstractQueryBuilderFactory<FuzzyQueryBuilder, Fuzzy> {
    public static final FuzzyQueryBuilderFactory INSTANCE = new FuzzyQueryBuilderFactory();

    private FuzzyQueryBuilderFactory() {
    }

    @Override
    protected void setOptionalMeta(FuzzyQueryBuilder builder, Fuzzy fuzzy) {
        builder.fuzziness(Fuzziness.build(fuzzy.fuzziness()))
                .maxExpansions(fuzzy.maxExpansions())
                .prefixLength(fuzzy.prefixLength());
    }

    @Override
    protected FuzzyQueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        return QueryBuilders.fuzzyQuery(conditionMeta.getName(), condition);
    }
}

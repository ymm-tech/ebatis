package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.Fuzzy;
import com.ymm.ebatis.meta.ConditionMeta;
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

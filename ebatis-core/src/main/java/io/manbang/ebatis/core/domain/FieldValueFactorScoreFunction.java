package io.manbang.ebatis.core.domain;

import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

class FieldValueFactorScoreFunction implements ScoreFunction {
    private final String fieldName;
    private float factor = FieldValueFactorFunctionBuilder.DEFAULT_FACTOR;
    private FieldValueFactorFunction.Modifier modifier = FieldValueFactorFunctionBuilder.DEFAULT_MODIFIER;
    private double missing = 0;

    FieldValueFactorScoreFunction(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValueFactorScoreFunction modifier(FieldValueFactorFunction.Modifier modifier) {
        this.modifier = modifier;
        return this;
    }

    public FieldValueFactorScoreFunction factor(float factor) {
        this.factor = factor;
        return this;
    }

    public FieldValueFactorScoreFunction missing(double missing) {
        this.missing = missing;
        return this;
    }

    @Override
    public FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder() {
        return new FunctionScoreQueryBuilder.FilterFunctionBuilder(new FieldValueFactorFunctionBuilder(fieldName).factor(factor).missing(missing).modifier(modifier));
    }
}

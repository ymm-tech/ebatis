package com.ymm.ebatis.core.domain;

import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;

class RandomScoreFunction implements ScoreFunction {
    static final RandomScoreFunction INSTANCE = new RandomScoreFunction();

    private RandomScoreFunction() {
    }

    @Override
    public FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder() {
        return new FunctionScoreQueryBuilder.FilterFunctionBuilder(new RandomScoreFunctionBuilder());
    }
}

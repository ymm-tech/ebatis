package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.builder.QueryBuilderFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.WeightBuilder;

/**
 * @author weilong.hu
 * @date 2020-04-13
 */
class WeightScoreFunction implements ScoreFunction {

    private final float weight;

    private final Object weightCondition;

    WeightScoreFunction(float weight, Object weightCondition) {
        this.weight = weight;
        this.weightCondition = weightCondition;
    }

    @Override
    public FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder() {
        QueryBuilder queryBuilder = QueryBuilderFactory.bool().create(null, weightCondition);
        return new FunctionScoreQueryBuilder.FilterFunctionBuilder(queryBuilder, new WeightBuilder().setWeight(weight));
    }
}

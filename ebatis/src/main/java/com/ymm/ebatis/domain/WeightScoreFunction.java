package com.ymm.ebatis.domain;

import com.ymm.ebatis.builder.BoolQueryBuilderFactory;
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

    public WeightScoreFunction(float weight, Object weightCondition) {
        this.weight = weight;
        this.weightCondition = weightCondition;
    }

    @Override
    public FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder() {
        QueryBuilder queryBuilder = BoolQueryBuilderFactory.INSTANCE.create(null, weightCondition);
        return new FunctionScoreQueryBuilder.FilterFunctionBuilder(queryBuilder, new WeightBuilder().setWeight(weight));
    }
}

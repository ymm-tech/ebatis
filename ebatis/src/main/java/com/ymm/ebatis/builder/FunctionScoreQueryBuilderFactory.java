package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.FunctionScore;
import com.ymm.ebatis.domain.ScoreFunction;
import com.ymm.ebatis.domain.ScoreFunctionMode;
import com.ymm.ebatis.core.domain.ScoreFunctionProvider;
import com.ymm.ebatis.exception.ConditionNotSupportException;
import com.ymm.ebatis.meta.ConditionMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author duoliang.zhang
 */
@Slf4j
public class FunctionScoreQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, FunctionScore> {
    public static final FunctionScoreQueryBuilderFactory INSTANCE = new FunctionScoreQueryBuilderFactory();

    private FunctionScoreQueryBuilderFactory() {
    }

    @Override
    protected void setOptionalMeta(QueryBuilder builder, FunctionScore functionScore) {
        // do nothing
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta<?> conditionMeta, Object condition) {
        if (!(condition instanceof ScoreFunctionProvider)) {
            throw new ConditionNotSupportException("条件必须实现: ScoreFunctionProvider");
        }

        QueryBuilder queryBuilder = BoolQueryBuilderFactory.INSTANCE.create(conditionMeta, condition);

        ScoreFunctionProvider provider = (ScoreFunctionProvider) condition;
        //对于函数积分组合查询，如果没有提供ScoreFunction,则当作Bool查询
        if (ArrayUtils.isEmpty(provider.getFunctions())) {
            return queryBuilder;
        }
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilders = Stream.of(provider.getFunctions())
                .map(ScoreFunction::toFunctionBuilder)
                .toArray(FunctionScoreQueryBuilder.FilterFunctionBuilder[]::new);
        FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(queryBuilder, functionBuilders);
        ScoreFunctionMode scoreFunctionMode = provider.getFunctionMode();
        Optional.ofNullable(scoreFunctionMode).ifPresent(sfm -> {
            functionScoreQueryBuilder.maxBoost(sfm.getMaxBoost()).setMinScore(sfm.getMinScore()).scoreMode(sfm.getScoreMode()).boostMode(sfm.getBoostMode());
        });
        return functionScoreQueryBuilder;
    }
}

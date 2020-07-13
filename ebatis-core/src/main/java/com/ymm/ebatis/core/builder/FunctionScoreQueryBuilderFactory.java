package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.annotation.FunctionScore;
import com.ymm.ebatis.core.domain.ScoreFunction;
import com.ymm.ebatis.core.domain.ScoreFunctionMode;
import com.ymm.ebatis.core.exception.ConditionNotSupportException;
import com.ymm.ebatis.core.meta.ConditionMeta;
import com.ymm.ebatis.core.provider.ScoreFunctionProvider;
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
class FunctionScoreQueryBuilderFactory extends AbstractQueryBuilderFactory<QueryBuilder, FunctionScore> {
    static final FunctionScoreQueryBuilderFactory INSTANCE = new FunctionScoreQueryBuilderFactory();

    private FunctionScoreQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(QueryBuilder builder, FunctionScore functionScore) {
        // do nothing
    }

    @Override
    protected QueryBuilder doCreate(ConditionMeta meta, Object condition) {
        if (!(condition instanceof ScoreFunctionProvider)) {
            throw new ConditionNotSupportException("条件必须实现: ScoreFunctionProvider");
        }

        QueryBuilder queryBuilder = BoolQueryBuilderFactory.INSTANCE.create(meta, condition);

        ScoreFunctionProvider provider = (ScoreFunctionProvider) condition;
        //对于函数积分组合查询，如果没有提供ScoreFunction,则当作Bool查询
        ScoreFunction[] functions = provider.getFunctions();
        if (ArrayUtils.isEmpty(functions)) {
            return queryBuilder;
        }
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilders = Stream.of(functions)
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

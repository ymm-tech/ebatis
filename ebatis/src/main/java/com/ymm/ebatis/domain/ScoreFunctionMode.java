package com.ymm.ebatis.domain;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;

/**
 * @author weilong.hu
 * @date 2020-04-17
 */
public interface ScoreFunctionMode {
    static ScoreFunctionMode of(FunctionScoreQuery.ScoreMode scoreMode, CombineFunction boostMode, float maxBoost, float minScore) {
        return new SimpleScoreFunctionMode(scoreMode, boostMode, maxBoost, minScore);
    }

    FunctionScoreQuery.ScoreMode getScoreMode();

    CombineFunction getBoostMode();

    float getMaxBoost();

    float getMinScore();
}

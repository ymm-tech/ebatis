package io.manbang.ebatis.core.domain;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;

/**
 * @author weilong.hu
 * @since 2020-04-17
 */
public class SimpleScoreFunctionMode implements ScoreFunctionMode {
    private final FunctionScoreQuery.ScoreMode scoreMode;
    private final CombineFunction boostMode;
    private final float maxBoost;
    private final float minScore;

    public SimpleScoreFunctionMode(FunctionScoreQuery.ScoreMode scoreMode, CombineFunction boostMode, float maxBoost, float minScore) {
        this.scoreMode = scoreMode;
        this.boostMode = boostMode;
        this.maxBoost = maxBoost;
        this.minScore = minScore;
    }

    @Override
    public FunctionScoreQuery.ScoreMode getScoreMode() {
        return scoreMode;
    }

    @Override
    public CombineFunction getBoostMode() {
        return boostMode;
    }

    @Override
    public float getMaxBoost() {
        return maxBoost;
    }

    @Override
    public float getMinScore() {
        return minScore;
    }
}

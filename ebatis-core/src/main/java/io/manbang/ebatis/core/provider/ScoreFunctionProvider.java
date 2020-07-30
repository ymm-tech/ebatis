package io.manbang.ebatis.core.provider;

import io.manbang.ebatis.core.domain.ScoreFunction;
import io.manbang.ebatis.core.domain.ScoreFunctionMode;

/**
 * 记分函数提供者
 *
 * @author 章多亮
 */
public interface ScoreFunctionProvider extends Provider {
    /**
     * 获取记分函数列表
     *
     * @return 函数列表
     */
    default ScoreFunction[] getFunctions() {
        ScoreFunction function = getFunction();
        if (function == null) {
            return new ScoreFunction[0];
        }

        return new ScoreFunction[]{function};
    }

    /**
     * 获取计分函数
     *
     * @return 计分函数
     */
    ScoreFunction getFunction();

    /**
     * 获取计分模式
     *
     * @return 计分模式
     */
    ScoreFunctionMode getFunctionMode();
}

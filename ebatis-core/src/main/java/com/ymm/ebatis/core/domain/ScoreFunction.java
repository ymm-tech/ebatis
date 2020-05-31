package com.ymm.ebatis.core.domain;

import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

/**
 * @author 章多亮
 * @since 2019/12/25 17:38
 */
public interface ScoreFunction {
    /**
     * 创建一个n内联脚本计分函数
     *
     * @param source 内联函数
     * @param params 参数
     * @return 计分函数
     */
    static ScoreFunction inlineScript(String source, Object params) {
        return new ScriptScoreFunction(Script.inline(source, params));
    }

    /**
     * 创建一个存储脚本函数
     *
     * @param id     脚本Id
     * @param params 参数
     * @return 计分函数
     */
    static ScoreFunction storedScript(String id, Object params) {
        return new ScriptScoreFunction(Script.stored(id, params));
    }

    /**
     * 随机打分函数
     *
     * @return 打分函数
     */
    static ScoreFunction randomScore() {
        return RandomScoreFunction.INSTANCE;
    }

    /**
     * 字段向量
     *
     * @param fieldName 字段名称
     * @param factor    影响因子
     * @param missing   默认值
     * @param modifier  修饰符
     * @return 打分函数
     */
    static ScoreFunction fieldValueFactor(String fieldName, float factor, double missing, FieldValueFactorFunction.Modifier modifier) {
        return new FieldValueFactorScoreFunction(fieldName).factor(factor).missing(missing).modifier(modifier);
    }

    /**
     *
     * @param weight
     * @param weightCondition
     * @return
     */
    static ScoreFunction weightScore(float weight, Object weightCondition) {
        return new WeightScoreFunction(weight, weightCondition);
    }

    /**
     * 叫脚本函数转换成脚本函数构建器
     *
     * @return 计分函数构建器
     */
    FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder();
}

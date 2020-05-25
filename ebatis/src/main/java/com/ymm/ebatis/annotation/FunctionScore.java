package com.ymm.ebatis.annotation;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionScore {
    /**
     * 配置计分模式，默认乘法
     *
     * @return 计分模式
     */
    FunctionScoreQuery.ScoreMode scoreMode() default FunctionScoreQuery.ScoreMode.MULTIPLY;

    CombineFunction boostMode() default CombineFunction.MULTIPLY;

    float maxBoost() default FunctionScoreQuery.DEFAULT_MAX_BOOST;

    float minScore() default Float.NaN;

    /**
     * @return 算法类型
     */
    ScoreType scoreType() default ScoreType.SCRIPT_SCORE;
}

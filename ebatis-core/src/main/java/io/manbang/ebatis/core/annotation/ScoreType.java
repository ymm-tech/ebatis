package io.manbang.ebatis.core.annotation;

/**
 * @author duoliang.zhang
 * @since 2019/12/13 18:07
 */
public enum ScoreType {
    /**
     * 脚本
     */
    SCRIPT_SCORE,
    /**
     * 权重
     */
    WEIGHT,
    /**
     * 随机
     */
    RANDOM_SCORE,
    /**
     * 字段值向量
     */
    FIELD_VALUE_FACTOR,
    /**
     * Decay functions score a document with a function that decays depending on the distance of a numeric field value of the document from a user given origin. This is similar to a range query, but with smooth edges instead of boxes.
     */
    DECAY_FUNCTION
}

package com.ymm.ebatis.core.annotation;

/**
 * 聚合类型
 *
 * @author 章多亮
 * @since 2020/1/2 16:43
 */
public enum AggType {
    /**
     * 指标聚合
     */
    METRIC,
    /**
     * 桶聚合
     */
    BUCKET,
    /**
     * 管道聚合
     */
    PIPELINE,
    /**
     * 矩阵聚合
     */
    MATRIX
}

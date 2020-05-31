package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 聚合注解
 *
 * @author duoliang.zhang
 * @since 2019/12/13 17:33
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Agg {
    boolean fetchSource() default false;

    int size() default 0;

    /**
     * 获取指标查询类型，这个类型决定了聚合时，会取哪个属性
     *
     * @return 聚合类型
     * @see Agg#metric() 指标聚合有意义
     * @see Agg#bucket()  分桶聚合有意义
     * @see Agg#matrix()  矩阵聚合有意义
     * @see Agg#pipeline()  管道聚合有意义
     */
    AggType type();

    /**
     * 获取指标查询注解
     *
     * @return 指标配置
     * @see AggType#METRIC
     */
    Metric[] metric() default {};

    /**
     * 获取分桶聚合注解配置
     *
     * @return 分桶配置
     * @see AggType#BUCKET
     */
    Bucket[] bucket() default {};

    /**
     * 获取分桶聚合注解配置
     *
     * @return 矩阵聚合配置
     * @see AggType#MATRIX
     */
    Matrix[] matrix() default {};

    /**
     * 获取管道聚合注解配置
     *
     * @return 管道配置
     * @see AggType#PIPELINE
     */
    Pipeline[] pipeline() default {};

    String routing() default AnnotationConstants.NO_SET;
}

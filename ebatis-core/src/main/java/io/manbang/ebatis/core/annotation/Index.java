package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
    /**
     * 创建索引的时候，可以指定Id
     *
     * @return id字段 名称
     */
    String id() default "";

    /**
     * 创建索引的方式
     *
     * @return 创建索引的方式
     */
    OpType opType() default OpType.INDEX;

    VersionType versionType() default VersionType.INTERNAL;

    String parent() default "";

    /**
     * 设置预处理管道名称，管道必须预先在集群中创建好
     *
     * @return 管道名称
     */
    String pipeline() default "";

    /**
     * 最后一个管道名称
     *
     * @return 管道名称
     */
    String finalPipeline() default "";

    /**
     * 数字 + 时间单位（s/m/h）
     *
     * @return 超时时间
     */
    String timeout() default "1m";

    /**
     * @return 刷新策略
     */
    RefreshPolicy refreshPolicy() default RefreshPolicy.NONE;

    boolean requireAlias() default false;

    /**
     * 获取等待的主分片和副本分片数量，默认只需要主分片活跃就可以
     *
     * @return 活跃分片数量
     */
    String waitForActiveShards() default "-2";
}

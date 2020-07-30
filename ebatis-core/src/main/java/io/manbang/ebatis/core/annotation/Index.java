package io.manbang.ebatis.core.annotation;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.VersionType;

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
    DocWriteRequest.OpType opType() default DocWriteRequest.OpType.INDEX;

    VersionType versionType() default VersionType.INTERNAL;

    String parent() default "";

    String pipeline() default "";

    /**
     * 数字 + 时间单位（s/m/h）
     *
     * @return 超时时间
     */
    String timeout() default "1m";

    WriteRequest.RefreshPolicy refreshPolicy() default WriteRequest.RefreshPolicy.NONE;

    /**
     * 获取等待的主分片和副本分片数量，默认只需要主分片活跃就可以
     *
     * @return 活跃分片数量
     */
    String waitForActiveShards() default "-2";
}

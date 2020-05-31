package com.ymm.ebatis.core.annotation;

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
    String id() default AnnotationConstants.NO_SET;

    /**
     * 创建索引的方式
     *
     * @return 创建索引的方式
     */
    DocWriteRequest.OpType opType() default DocWriteRequest.OpType.INDEX;

    VersionType versionType() default VersionType.INTERNAL;

    /**
     * 获取路由字段，可以不指定，默认<code>_id</code>
     *
     * @return 路由字段
     */
    String routing() default AnnotationConstants.NO_SET;

    String parent() default AnnotationConstants.NO_SET;

    String pipeline() default AnnotationConstants.NO_SET;

    /**
     * 数字 + 时间单位（s/m/h）
     *
     * @return 超时时间
     */
    String timeout() default "1m";

    WriteRequest.RefreshPolicy refreshPolicy() default WriteRequest.RefreshPolicy.NONE;

    String waitForActiveShards() default "1";
}

package com.ymm.ebatis.core.annotation;

import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.VersionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 * @since 2019/12/13 17:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Update {
    String id() default AnnotationConstants.NO_SET;

    String routing() default AnnotationConstants.NO_SET;

    boolean fetchSource() default false;

    String parent() default AnnotationConstants.NO_SET;

    long timeout() default AnnotationConstants.TIMEOUT_NO_SET;

    WriteRequest.RefreshPolicy refreshPolicy() default WriteRequest.RefreshPolicy.NONE;

    boolean scriptedUpsert() default false;

    boolean docAsUpsert() default false;

    boolean detectNoop() default true;

    int retryOnConflict() default 0;

    VersionType versionType() default VersionType.INTERNAL;

    int waitForActiveShards() default AnnotationConstants.ACTIVE_SHARD_COUNT_DEFAULT;
}

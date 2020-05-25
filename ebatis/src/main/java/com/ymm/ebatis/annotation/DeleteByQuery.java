package com.ymm.ebatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询删除注解
 *
 * @author 章多亮
 * @since 2019/12/28 14:50:02
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteByQuery {
    int maxDocs() default -1;

    int batchSize() default 1000;

    String conflicts() default "abort";

    int slices() default 1;

    String timeout() default "1m";

    boolean refresh() default false;

    int maxRetries() default 11;

    int waitForActiveShards() default AnnotationConstants.ACTIVE_SHARD_COUNT_DEFAULT;

    boolean shouldStoreResult() default false;

    long scrollKeepAlive() default 0;

    String routing() default AnnotationConstants.NO_SET;
}

package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2019/12/30 14:38:21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateByQuery {
    int maxDocs() default -1;

    int batchSize() default 1000;

    String conflicts() default "abort";

    int slices() default 1;

    String timeout() default "1m";

    boolean refresh() default false;

    int maxRetries() default 11;

    String waitForActiveShards() default "-2";

    boolean shouldStoreResult() default false;

    long scrollKeepAlive() default 0;

    String routing() default AnnotationConstants.NO_SET;
}

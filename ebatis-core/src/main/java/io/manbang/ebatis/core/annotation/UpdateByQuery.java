package io.manbang.ebatis.core.annotation;

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
    String preference() default "";

    String expandWildcards() default "open";

    int from() default 0;

    boolean requestCache() default false;

    boolean ignoreUnavailable() default false;

    boolean analyzeWildcard() default false;

    String df() default "";

    String analyzer() default "";

    boolean lenient() default false;

    boolean allowNoIndices() default true;

    float requestsPerSecond() default Float.POSITIVE_INFINITY;

    String[] docTypes() default {};

    int batchSize() default 1000;

    String conflicts() default "abort";

    int slices() default 1;

    String timeout() default "1m";

    String masterTimeout() default "30s";

    boolean version() default false;

    boolean refresh() default false;

    int maxRetries() default 11;

    String waitForActiveShards() default "-2";

    boolean shouldStoreResult() default false;

    long scrollKeepAlive() default 0;
}

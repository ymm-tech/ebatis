package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reindex {
    String destination();

    QueryType queryType() default QueryType.MATCH_ALL;

    boolean waitForCompletion() default true;

    String conflicts() default "abort";

    int slices() default 1;

    int batchSize() default 1000;

    int maxDocs() default -1;

    float requestsPerSecond() default Float.POSITIVE_INFINITY;

    boolean refresh() default false;

    String timeout() default "1m";

    String pipeline() default "";

    String opType() default "index";

    VersionType versionType() default VersionType.INTERNAL;

    boolean requireAlias() default false;
}

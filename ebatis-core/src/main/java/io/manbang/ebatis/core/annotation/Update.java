package io.manbang.ebatis.core.annotation;

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
    String id() default "";

    boolean fetchSource() default false;

    String parent() default "";

    String timeout() default "";

    String refreshPolicy() default "false";

    boolean scriptedUpsert() default false;

    boolean docAsUpsert() default false;

    boolean detectNoop() default true;

    int retryOnConflict() default 0;

    VersionType versionType() default VersionType.INTERNAL;

    String waitForActiveShards() default "-2";
}

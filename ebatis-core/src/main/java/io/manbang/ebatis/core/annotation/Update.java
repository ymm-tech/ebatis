package io.manbang.ebatis.core.annotation;

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
    boolean fetchSource() default false;

    String timeout() default "1m";

    RefreshPolicy refreshPolicy() default RefreshPolicy.NONE;

    boolean scriptedUpsert() default false;

    boolean docAsUpsert() default false;

    boolean detectNoop() default true;

    int retryOnConflict() default 0;

    boolean requireAlias() default false;

    String waitForActiveShards() default "-2";
}

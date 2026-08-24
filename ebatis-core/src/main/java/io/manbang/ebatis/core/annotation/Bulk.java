package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2019/12/26 19:42
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bulk {
    BulkType bulkType();

    String timeout() default "1m";

    String waitForActiveShards() default "-2";

    String refreshPolicy() default "false";

    Index[] index() default {};

    Delete[] delete() default {};

    Update[] update() default {};

    int batchSize() default 1000;

    int concurrentRequests() default 1;

    int maxRetries() default 3;

    String retryBackoff() default "100ms";
}

package com.ymm.ebatis.core.annotation;

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

    String refreshPolicy() default "NONE";

    Index[] index() default {};

    Delete[] delete() default {};

    Update[] update() default {};
}

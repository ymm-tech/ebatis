package io.manbang.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bool {
    /**
     * 如果可能是否需要简化dsl语句
     *
     * @return 简化，则返回<code>true</code>，否则，返回<code>false</code>
     */
    boolean simplifyIfNecessary() default true;
}

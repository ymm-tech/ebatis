package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多get查询
 *
 * @author weilong.hu
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiGet {
    /**
     * 设置查询偏好，影响查询的分片策略
     *
     * @return 查询偏好
     */
    String preference() default "";

    /**
     * 设置是否刷新，默认不刷新
     *
     * @return 刷新，设置为<code>true</code>
     */
    boolean refresh() default false;

    /**
     * 设置是否实时查询，默认实时
     *
     * @return 实时，设置为<code>true</code>
     */
    boolean realtime() default true;
}

package com.ymm.ebatis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http请求配置
 *
 * @author 章多亮
 * @since 2020/6/1 11:29
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Http {
    /**
     * 创建ES连接的超时间，单位毫秒，默认 3s
     *
     * @return 超时间，秒
     */
    int connectTimeout() default 1000;

    /**
     * 请求响应超时时间，单位毫秒，默认 5s
     *
     * @return 超时间
     */
    int socketTimeout() default 30000;

    /**
     * 请求连接池返回连接的超时时间，单位毫秒
     *
     * @return 超时间
     */
    int connectionRequestTimeout() default -1;

    /**
     * 一个目标路由的最大连接数
     *
     * @return 每个路由最大连接数
     */
    int maxConnectionPerRoute() default 10;

    /**
     * 连接池最大连接数
     *
     * @return 最大连接数
     */
    int maxConnectionTotal() default 30;
}


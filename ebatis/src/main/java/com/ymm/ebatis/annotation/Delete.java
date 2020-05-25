package com.ymm.ebatis.annotation;

import org.elasticsearch.action.support.WriteRequest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {
    String routing() default AnnotationConstants.NO_SET;

    /**
     * 超时时间，单位(s/m/h/d)
     *
     * @return 时间
     */
    String timeout() default "1m";

    /**
     * 获取刷新策略
     *
     * @return 刷新策略
     */
    WriteRequest.RefreshPolicy refreshPolicy() default WriteRequest.RefreshPolicy.NONE;
}

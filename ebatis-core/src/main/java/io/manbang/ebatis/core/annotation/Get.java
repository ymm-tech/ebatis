package io.manbang.ebatis.core.annotation;

import org.elasticsearch.index.VersionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/5/23 18:09
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Get {
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

    /**
     * 设置版本类型
     *
     * @return 版本类型
     */
    VersionType versionType() default VersionType.INTERNAL;
}

package com.ymm.ebatis.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duoliang.zhang
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EsMapperRegistrar.class)
public @interface EnableEsMapper {
    /**
     * 获取需要扫描的包路径
     *
     * @return 待扫描的包路径
     */
    String[] value() default {};

    /**
     * 获取需要扫描的包路径
     *
     * @return 待扫描的包路径
     */
    String[] basePackages() default {};

    /**
     * 获取集群路由器名称
     *
     * @return 集群路由器名称
     */
    String clusterRouter() default "clusterRouter";
}

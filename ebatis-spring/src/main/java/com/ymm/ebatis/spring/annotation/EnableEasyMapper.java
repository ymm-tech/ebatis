package com.ymm.ebatis.spring.annotation;

import com.ymm.ebatis.spring.cluster.SpringClusterRouterProvider;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用ebatis
 *
 * @author duoliang.zhang
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EasyMapperRegistrar.class, SpringClusterRouterProvider.class})
public @interface EnableEasyMapper {
    /**
     * 获取需要扫描的包路径
     *
     * @return 待扫描的包路径
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 获取需要扫描的包路径
     *
     * @return 待扫描的包路径
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * 获取集群路由器名称，默认是本地的测试进群，【127.0.0.1:9200】
     *
     * @return 集群路由器名称
     */
    String clusterRouter() default "clusterRouter";
}

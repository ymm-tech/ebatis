package io.manbang.ebatis.spring.annotation;

import io.manbang.ebatis.core.annotation.Mapper;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2019/12/16 17:21
 */
@Repository
@Documented
@Mapper
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyMapper {
    /**
     * 一此操作可以指定多查询的索引，但是不建议这么使用，所以只能指定一个索引
     *
     * @return 索引
     */
    @AliasFor(attribute = "indices", annotation = Mapper.class)
    String[] indices() default {};

    /**
     * 获取路由信息
     *
     * @return 路由
     */
    @AliasFor(attribute = "types", annotation = Mapper.class)
    String[] types() default {};

    /**
     * 获取Mapper独立的集群
     *
     * @return 集群名称
     */
    @AliasFor(attribute = "clusterRouter", annotation = Mapper.class)
    String clusterRouter() default "";
}

package com.ymm.ebatis.core.annotation;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoValidationMethod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 章多亮
 * @since 2020/1/7 14:25
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeoDistance {
    /**
     * 获取距离单位
     *
     * @return 距离单位
     */
    DistanceUnit unit() default DistanceUnit.KILOMETERS;

    /**
     * Algorithm to use for distance computation. PLANE/ARC
     */
    String distanceCalculation() default "ARC";

    /**
     * How strict should geo coordinate validation be?
     */
    GeoValidationMethod validationMethod() default GeoValidationMethod.STRICT;

    boolean ignoreUnmapped() default false;
}

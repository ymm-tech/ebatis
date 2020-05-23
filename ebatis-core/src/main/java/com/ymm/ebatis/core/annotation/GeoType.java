package com.ymm.ebatis.core.annotation;

/**
 * @author 章多亮
 * @since 2020/1/7 10:47
 */
public enum GeoType {
    /**
     * GEO形状
     */
    GEO_SHAPE,
    /**
     * 距离查询
     */
    GEO_DISTANCE,
    GEO_DISTANCE_RANGE,
    /**
     * 矩形查询
     */
    GEO_BOUNDING_BOX,
    GEO_POLYGON
}

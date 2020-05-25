package com.ymm.ebatis.domain;

import org.elasticsearch.common.unit.DistanceUnit;

/**
 * @author 章多亮
 * @since 2020/1/7 19:15
 */
public interface GeoRange {
    static GeoRange geoDistance(Coordinate center, double distance, DistanceUnit unit) {
        return null;
    }
}

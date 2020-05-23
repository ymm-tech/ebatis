package com.ymm.ebatis.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;

/**
 * @author 章多亮
 * @since 2020/1/8 17:11
 */
@Data
@AllArgsConstructor
public class Coordinate {
    private final double x;
    private final double y;
    private final double z;

    public Coordinate(double x, double y) {
        this(x, y, 0.0);
    }

    public Coordinate() {
        this(0.0, 0.0, 0.0);
    }


    public GeoPoint toPoint() {
        return new GeoPoint(x, y);
    }
}

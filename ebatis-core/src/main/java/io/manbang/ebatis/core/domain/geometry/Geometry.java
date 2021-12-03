package io.manbang.ebatis.core.domain.geometry;

import io.manbang.ebatis.core.domain.Coordinate;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:05
 */
public interface Geometry {
    static Geometry circle(Coordinate coordinate, String radius) {
        return new CircleGeometry(coordinate, radius);
    }

    static Geometry line(Coordinate[] points) {
        return new LineGeometry(points);
    }

    static Geometry point(Coordinate coordinate) {
        return new PointGeometry(coordinate);
    }

}

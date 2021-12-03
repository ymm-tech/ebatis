package io.manbang.ebatis.core.domain;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:05
 */
public interface Geometry {
    static Geometry circle(Coordinate coordinate, final double radiusMeters) {
        return new CircleGeometry(coordinate, radiusMeters);
    }

    static Geometry line(Coordinate[] points) {
        return new LineGeometry(points);
    }

    static Geometry point(Coordinate coordinate) {
        return new PointGeometry(coordinate);
    }


    static Geometry rectangle(Coordinate min, Coordinate max) {
        return new RectangleGeometry(min, max);
    }
}

package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.geometry.Line;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:19
 */
class LineGeometry implements Geometry, BuildProvider {
    private final Coordinate[] points;


    LineGeometry(Coordinate[] points) {
        if (Objects.isNull(points)) {
            throw new IllegalArgumentException("points must not be null");
        }
        this.points = points;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        final int length = points.length;
        double[] x = new double[length];
        double[] y = new double[length];
        double[] z = new double[length];
        for (int i = 0; i < length; i++) {
            final Coordinate point = points[i];
            x[i] = point.getX();
            y[i] = point.getY();
            z[i] = point.getZ();
        }
        return (T) new Line(x, y, z);
    }
}

package io.manbang.ebatis.core.geometry;

import io.manbang.ebatis.core.domain.Coordinate;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.geometry.Circle;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:08
 */
public class CircleGeometry implements Geometry, BuildProvider {
    private final double y;
    private final double x;
    private final double z;
    private final double radiusMeters;

    CircleGeometry(Coordinate point, final double radiusMeters) {
        if (Objects.isNull(point)) {
            throw new IllegalArgumentException("point must not be null");
        }
        this.x = point.getX();
        this.y = point.getY();
        this.z = point.getZ();
        this.radiusMeters = radiusMeters;
        if (radiusMeters < 0) {
            throw new IllegalArgumentException("Circle radius [" + radiusMeters + "] cannot be negative");
        }
    }


    @Override
    public <T> T build() {
        return (T) new Circle(y, x, z, radiusMeters);
    }
}

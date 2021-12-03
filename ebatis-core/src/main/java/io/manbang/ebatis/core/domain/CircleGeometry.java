package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.common.geo.builders.CircleBuilder;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:08
 */
class CircleGeometry implements Geometry, BuildProvider {
    private final double x;
    private final double y;
    private final String radius;

    CircleGeometry(Coordinate point, final String radius) {
        if (Objects.isNull(point)) {
            throw new IllegalArgumentException("point must not be null");
        }
        this.x = point.getX();
        this.y = point.getY();
        this.radius = radius;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) new CircleBuilder().center(x, y).radius(radius);
    }
}

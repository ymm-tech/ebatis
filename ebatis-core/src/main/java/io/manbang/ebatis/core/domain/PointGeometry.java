package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.geometry.Point;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:21
 */
class PointGeometry implements Geometry, BuildProvider {
    private Coordinate coordinate;

    PointGeometry(Coordinate coordinate) {
        if (Objects.isNull(coordinate)) {
            throw new IllegalArgumentException("points must not be null");
        }
        this.coordinate = coordinate;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        return (T) new Point(coordinate.getX(), coordinate.getY(), coordinate.getZ());
    }
}

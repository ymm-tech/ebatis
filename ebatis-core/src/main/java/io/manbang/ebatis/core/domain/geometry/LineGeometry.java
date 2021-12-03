package io.manbang.ebatis.core.domain.geometry;

import io.manbang.ebatis.core.domain.Coordinate;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.common.geo.builders.LineStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:19
 */
public class LineGeometry implements Geometry, BuildProvider {
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
        List<org.locationtech.jts.geom.Coordinate> coordinates = new ArrayList<>();
        for (Coordinate point : points) {
            coordinates.add(new org.locationtech.jts.geom.Coordinate(point.getX(), point.getY(), point.getZ()));
        }
        return (T) new LineStringBuilder(coordinates);
    }
}

package io.manbang.ebatis.core.geometry;

import io.manbang.ebatis.core.domain.Coordinate;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.geometry.Rectangle;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:27
 */
public class RectangleGeometry implements Geometry, BuildProvider {

    private final Coordinate min;
    private final Coordinate max;

    RectangleGeometry(Coordinate min, Coordinate max) {
        if (Objects.isNull(min)) {
            throw new IllegalArgumentException("min must not be null");
        }
        if (Objects.isNull(max)) {
            throw new IllegalArgumentException("max must not be null");
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public <T> T build() {
        return (T) new Rectangle(min.getX(), max.getX(), max.getY(), min.getY(), min.getZ(), max.getZ());
    }
}

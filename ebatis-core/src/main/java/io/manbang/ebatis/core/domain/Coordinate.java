package io.manbang.ebatis.core.domain;

import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/1/8 17:11
 */
@Data
public class Coordinate {
    private final double x;
    private final double y;
    private final double z;

    public Coordinate(double x, double y) {
        this(x, y, Double.NaN);
    }

    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

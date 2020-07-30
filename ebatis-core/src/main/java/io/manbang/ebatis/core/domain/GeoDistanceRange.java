package io.manbang.ebatis.core.domain;

import lombok.Data;

/**
 * @author 章多亮
 * @since 2020/1/7 19:16
 */
@Data
public class GeoDistanceRange implements GeoRange {
    private final Coordinate center;
    private final String distance;
}

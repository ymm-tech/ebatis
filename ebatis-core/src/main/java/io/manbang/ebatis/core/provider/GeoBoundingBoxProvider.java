package io.manbang.ebatis.core.provider;

import org.elasticsearch.common.geo.GeoPoint;

public interface GeoBoundingBoxProvider extends Provider {
    GeoPoint topLeft();

    GeoPoint bottomRight();
}

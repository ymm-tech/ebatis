package io.manbang.ebatis.core.provider;

import org.elasticsearch.common.geo.GeoPoint;

import java.util.List;

public interface GeoPolygonProvider extends Provider {
    List<GeoPoint> points();
}

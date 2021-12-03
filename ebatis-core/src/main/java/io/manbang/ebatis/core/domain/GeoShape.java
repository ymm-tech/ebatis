package io.manbang.ebatis.core.domain;

public interface GeoShape {
    static GeoShape geoShape(String name, Geometry shape) {
        return new DefaultGeoShape(name, shape);
    }

    static GeoShape indexedShape(String name, String indexedShapeId, String indexedShapeType) {
        return new DefaultGeoShape(name, indexedShapeId, indexedShapeType);
    }

    GeoShape intersects();

    GeoShape disjoint();

    GeoShape within();

    GeoShape contains();
}
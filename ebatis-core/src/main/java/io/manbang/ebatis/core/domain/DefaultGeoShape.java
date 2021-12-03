package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.geometry.Geometry;
import io.manbang.ebatis.core.geometry.ShapeRelation;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

public class DefaultGeoShape implements GeoShape, BuildProvider {
    private static final ShapeRelation DEFAULT_SHAPE_RELATION = ShapeRelation.INTERSECTS;
    private final String name;
    private Geometry shape;
    private String indexedShapeId;
    private final ShapeRelation relation = DEFAULT_SHAPE_RELATION;

    public DefaultGeoShape(String name, Geometry shape) {
        this.name = name;
        this.shape = shape;
    }

    public DefaultGeoShape(String name, String indexedShapeId) {
        this.name = name;
        this.indexedShapeId = indexedShapeId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        if (shape == null) {
            return (T) QueryBuilders.geoShapeQuery(name, indexedShapeId).relation(relation.build());
        } else {
            final org.elasticsearch.geometry.Geometry build = ((BuildProvider) shape).build();
            try {
                return (T) QueryBuilders.geoShapeQuery(name, build).relation(relation.build());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
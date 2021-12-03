package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.domain.geometry.Geometry;
import io.manbang.ebatis.core.provider.BuildProvider;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

import static org.elasticsearch.common.geo.ShapeRelation.CONTAINS;
import static org.elasticsearch.common.geo.ShapeRelation.DISJOINT;
import static org.elasticsearch.common.geo.ShapeRelation.INTERSECTS;
import static org.elasticsearch.common.geo.ShapeRelation.WITHIN;

public class DefaultGeoShape implements GeoShape, BuildProvider {
    private final String name;
    private ShapeRelation relation = INTERSECTS;
    private Geometry shape;
    private String indexedShapeId;

    public DefaultGeoShape(String name, Geometry shape) {
        this.name = name;
        this.shape = shape;
    }

    public DefaultGeoShape(String name, String indexedShapeId) {
        this.name = name;
        this.indexedShapeId = indexedShapeId;
    }

    @Override
    public GeoShape intersects() {
        this.relation = INTERSECTS;
        return this;
    }

    @Override
    public GeoShape disjoint() {
        this.relation = DISJOINT;
        return this;
    }

    @Override
    public GeoShape within() {
        this.relation = WITHIN;
        return this;
    }

    @Override
    public GeoShape contains() {
        this.relation = CONTAINS;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T build() {
        if (shape == null) {
            return (T) QueryBuilders.geoShapeQuery(name, indexedShapeId).relation(relation);
        } else {
            final org.elasticsearch.geometry.Geometry build = ((BuildProvider) shape).build();
            try {
                return (T) QueryBuilders.geoShapeQuery(name, build).relation(relation);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
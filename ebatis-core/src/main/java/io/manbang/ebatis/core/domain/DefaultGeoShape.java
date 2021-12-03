package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.geometry.Geometry;
import io.manbang.ebatis.core.geometry.ShapeRelation;
import io.manbang.ebatis.core.provider.BuildProvider;
import lombok.SneakyThrows;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

public class DefaultGeoShape implements GeoShape, BuildProvider {
    private static final ShapeRelation DEFAULT_SHAPE_RELATION = ShapeRelation.INTERSECTS;
    private String name;
    private Geometry shape;
    private String indexedShapeId;
    private ShapeRelation relation = DEFAULT_SHAPE_RELATION;

    public DefaultGeoShape(String name, Geometry shape) {
        this.name = name;
        this.shape = shape;
    }

    public DefaultGeoShape(String name, String indexedShapeId) {
        this.name = name;
        this.indexedShapeId = indexedShapeId;
    }

    @Override
    public ShapeRelation getRelation() {
        return relation;
    }

    @Override
    public GeoShape setRelation(ShapeRelation relation) {
        this.relation = relation;
        return this;
    }

    @SneakyThrows
    @Override
    public <T> T build() {
        if (Objects.nonNull(shape)) {
            final org.elasticsearch.geometry.Geometry build = ((BuildProvider) shape).build();
            return (T) QueryBuilders.geoShapeQuery(name, build).relation(relation.build());
        } else {
            return (T) QueryBuilders.geoShapeQuery(name, indexedShapeId).relation(relation.build());
        }
    }
}
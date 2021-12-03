package io.manbang.ebatis.core.geometry;

import io.manbang.ebatis.core.provider.BuildProvider;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:48
 */
public enum ShapeRelation implements BuildProvider {
    INTERSECTS(org.elasticsearch.common.geo.ShapeRelation.INTERSECTS),

    DISJOINT(org.elasticsearch.common.geo.ShapeRelation.DISJOINT),

    WITHIN(org.elasticsearch.common.geo.ShapeRelation.WITHIN),

    CONTAINS(org.elasticsearch.common.geo.ShapeRelation.CONTAINS);

    private final org.elasticsearch.common.geo.ShapeRelation shapeRelation;

    ShapeRelation(org.elasticsearch.common.geo.ShapeRelation shapeRelation) {
        this.shapeRelation = shapeRelation;
    }

    @Override
    public <T> T build() {
        return (T) shapeRelation;
    }
}

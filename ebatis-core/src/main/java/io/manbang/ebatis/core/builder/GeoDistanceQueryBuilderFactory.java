package io.manbang.ebatis.core.builder;

import io.manbang.ebatis.core.annotation.GeoDistance;
import io.manbang.ebatis.core.domain.Coordinate;
import io.manbang.ebatis.core.domain.GeoDistanceRange;
import io.manbang.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/8 17:03
 */
class GeoDistanceQueryBuilderFactory extends AbstractQueryBuilderFactory<GeoDistanceQueryBuilder, GeoDistance> {
    static final GeoDistanceQueryBuilderFactory INSTANCE = new GeoDistanceQueryBuilderFactory();

    private GeoDistanceQueryBuilderFactory() {
    }

    @Override
    protected void setAnnotationMeta(GeoDistanceQueryBuilder builder, GeoDistance annotation) {
        builder.setValidationMethod(annotation.validationMethod());
        builder.ignoreUnmapped(annotation.ignoreUnmapped());
    }

    @Override
    protected GeoDistanceQueryBuilder doCreate(ConditionMeta meta, Object condition) {
        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery(meta.getName());

        if (condition instanceof GeoDistanceRange) {
            GeoDistanceRange distanceRange = (GeoDistanceRange) condition;
            final Coordinate center = distanceRange.getCenter();
            final GeoPoint geoPoint = new GeoPoint(center.getX(), center.getY());
            builder.distance(distanceRange.getDistance()).point(geoPoint).geoDistance(distanceRange.getGeoDistance());
        } else if (condition instanceof String) {
            builder.distance(String.valueOf(condition));
        } else if (condition instanceof Double) {
            meta.findAttributeAnnotation(GeoDistance.class)
                    .ifPresent(geoDistance -> builder.distance((Double) condition, geoDistance.unit()));
        }

        return builder;
    }
}

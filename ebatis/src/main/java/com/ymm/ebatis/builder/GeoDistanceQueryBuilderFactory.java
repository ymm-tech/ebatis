package com.ymm.ebatis.builder;

import com.ymm.ebatis.annotation.GeoDistance;
import com.ymm.ebatis.domain.GeoDistanceRange;
import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @author 章多亮
 * @since 2020/1/8 17:03
 */
public class GeoDistanceQueryBuilderFactory extends AbstractQueryBuilderFactory<GeoDistanceQueryBuilder, GeoDistance> {
    public static final GeoDistanceQueryBuilderFactory INSTANCE = new GeoDistanceQueryBuilderFactory();

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
            builder.distance(distanceRange.getDistance()).point(distanceRange.getCenter().toPoint());
        } else if (condition instanceof String) {
            builder.distance(String.valueOf(condition));
        } else if (condition instanceof Double) {
            meta.findAttributeAnnotation(GeoDistance.class)
                    .ifPresent(geoDistance -> builder.distance((Double) condition, geoDistance.unit()));
        }

        return builder;
    }
}

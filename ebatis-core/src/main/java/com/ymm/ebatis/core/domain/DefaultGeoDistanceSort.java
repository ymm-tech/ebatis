package com.ymm.ebatis.core.domain;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoValidationMethod;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.search.sort.GeoDistanceSortBuilder.DEFAULT_VALIDATION;

/**
 * @author 章多亮
 * @since 2020/1/6 18:57
 */
class DefaultGeoDistanceSort extends AbstractSort implements GeoDistanceSort {
    private final List<GeoPoint> points = new ArrayList<>();
    private GeoDistance geoDistance = GeoDistance.ARC;
    private DistanceUnit unit = DistanceUnit.DEFAULT;
    private GeoValidationMethod validation = DEFAULT_VALIDATION;
    private boolean ignoreUnmapped = false;

    DefaultGeoDistanceSort(String name, SortDirection direction) {
        super(name, direction);
    }

    @Override
    public GeoDistance geoDistance() {
        return geoDistance;
    }

    @Override
    public GeoDistanceSort geoDistance(GeoDistance distance) {
        this.geoDistance = distance;
        return this;
    }

    @Override
    public DistanceUnit unit() {
        return unit;
    }

    @Override
    public GeoDistanceSort unit(DistanceUnit unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public GeoValidationMethod validation() {
        return validation;
    }

    @Override
    public GeoDistanceSort validation(GeoValidationMethod validation) {
        this.validation = validation;
        return this;
    }

    @Override
    public boolean ignoreUnmapped() {
        return ignoreUnmapped;
    }

    @Override
    public GeoDistanceSort ignoreUnmapped(boolean ignoreUnmapped) {
        this.ignoreUnmapped = ignoreUnmapped;
        return this;
    }


    @Override
    public List<GeoPoint> points() {
        return Collections.unmodifiableList(points);
    }

    @Override
    public GeoDistanceSort addPoint(GeoPoint point) {
        this.points.add(point);
        return this;
    }

    @Override
    public GeoDistanceSort addPoint(double lat, double lon) {
        return addPoint(new GeoPoint(lat, lon));
    }

    @Override
    public GeoDistanceSort addPoint(String value) {
        return addPoint(new GeoPoint(value));
    }

    @Override
    public SortBuilder<?> toSortBuilder() {
        GeoDistanceSortBuilder builder = SortBuilders.geoDistanceSort(name(), points().toArray(new GeoPoint[0]))
                .ignoreUnmapped(ignoreUnmapped)
                .geoDistance(geoDistance)
                .unit(unit)
                .order(direction().getOrder());

        setSortMode(builder::sortMode);
        return builder;
    }
}

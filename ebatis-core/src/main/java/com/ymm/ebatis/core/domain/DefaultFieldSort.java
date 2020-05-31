package com.ymm.ebatis.core.domain;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

class DefaultFieldSort extends AbstractSort implements FieldSort {
    private Object missing;
    private String unmappedType;

    DefaultFieldSort(String name, SortDirection direction) {
        super(name, direction);
    }

    @Override
    public SortBuilder<?> toSortBuilder() {
        FieldSortBuilder builder = SortBuilders.fieldSort(name())
                .missing(missing)
                .unmappedType(unmappedType)
                .order(direction().getOrder());

        setSortMode(builder::sortMode);
        return builder;
    }


    @Override
    public Object missing() {
        return missing;
    }

    @Override
    public FieldSort missing(Object missing) {
        this.missing = missing;
        return this;
    }

    @Override
    public String unmappedType() {
        return unmappedType;
    }

    @Override
    public FieldSort unmappedType(String unmappedType) {
        this.unmappedType = unmappedType;
        return this;
    }
}

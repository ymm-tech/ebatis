package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.builder.QueryBuilderFactory;
import org.elasticsearch.search.sort.NestedSortBuilder;

import java.util.Objects;

/**
 * @author weilong.hu
 * @since 2021/1/11 13:54
 */
class DefaultNestedSort implements NestedSort {
    final private String path;
    private Object condition;
    private int maxChildren = Integer.MAX_VALUE;
    private NestedSort nestedSort;

    DefaultNestedSort(String path) {
        this.path = path;
    }

    @Override
    public NestedSort filter(Object condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public NestedSort maxChildren(int maxChildren) {
        this.maxChildren = maxChildren;
        return this;
    }

    @Override
    public NestedSort nested(NestedSort nestedSort) {
        this.nestedSort = nestedSort;
        return this;
    }

    @Override
    public NestedSortBuilder toNestedSortBuilder() {
        final NestedSortBuilder nestedSortBuilder = new NestedSortBuilder(path);
        nestedSortBuilder.setMaxChildren(maxChildren);
        if (Objects.nonNull(condition)) {
            nestedSortBuilder.setFilter(QueryBuilderFactory.bool().create(null, condition));
        }
        if (Objects.nonNull(nestedSort)) {
            nestedSortBuilder.setNestedSort(nestedSort.toNestedSortBuilder());
        }
        return nestedSortBuilder;
    }
}

package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.sort.NestedSortBuilder;

/**
 * @author weilong.hu
 * @since 2021/1/11 13:54
 */
public interface NestedSort {
    static NestedSort of(String path) {
        return new DefaultNestedSort(path);
    }

    NestedSort filter(final Object condition);

    NestedSort maxChildren(final int maxChildren);

    NestedSort nested(final NestedSort nestedSort);

    NestedSortBuilder toNestedSortBuilder();
}

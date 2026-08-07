package io.manbang.ebatis.core.domain;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

/**
 * Elasticsearch 相关性分数排序。
 */
class DefaultScoreSort extends AbstractSort {
    DefaultScoreSort(SortDirection direction) {
        super("_score", direction);
    }

    @Override
    public SortBuilder<?> toSortBuilder() {
        return SortBuilders.scoreSort().order(direction().getOrder());
    }
}

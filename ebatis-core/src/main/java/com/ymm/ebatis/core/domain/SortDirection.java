package com.ymm.ebatis.core.domain;

import org.elasticsearch.search.sort.SortOrder;

/**
 * 排序方向
 *
 * @author duoliang.zhang
 */
public enum SortDirection {
    /**
     * 升序
     */
    ASC(SortOrder.ASC),
    /**
     * 降序
     */
    DESC(SortOrder.DESC);

    /**
     * ES排序方向
     */
    private final SortOrder order;

    SortDirection(SortOrder order) {
        this.order = order;
    }

    public SortOrder getOrder() {
        return order;
    }
}

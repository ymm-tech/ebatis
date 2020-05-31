package com.ymm.ebatis.core.annotation;

/**
 * @author weilong.hu
 */
public enum BucketOrder {
    COUNT_ASC(org.elasticsearch.search.aggregations.BucketOrder.count(true)),
    COUNT_DESC(org.elasticsearch.search.aggregations.BucketOrder.count(false)),
    KEY_ASC(org.elasticsearch.search.aggregations.BucketOrder.key(true)),
    KEY_DESC(org.elasticsearch.search.aggregations.BucketOrder.key(false));

    private final org.elasticsearch.search.aggregations.BucketOrder order;

    BucketOrder(org.elasticsearch.search.aggregations.BucketOrder order) {
        this.order = order;
    }

    public org.elasticsearch.search.aggregations.BucketOrder order() {
        return this.order;
    }
}

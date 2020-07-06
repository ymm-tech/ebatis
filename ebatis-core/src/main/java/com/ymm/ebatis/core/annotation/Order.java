package com.ymm.ebatis.core.annotation;

import org.elasticsearch.search.aggregations.BucketOrder;

/**
 * @author weilong.hu
 */
public enum Order {
    /**
     * count升序
     */
    COUNT_ASC(BucketOrder.count(true)),
    /**
     * count降序
     */
    COUNT_DESC(BucketOrder.count(false)),
    /**
     * count升序
     */
    KEY_ASC(BucketOrder.key(true)),
    /**
     * count降序
     */
    KEY_DESC(BucketOrder.key(false));

    private final BucketOrder order;

    Order(BucketOrder order) {
        this.order = order;
    }

    public BucketOrder order() {
        return this.order;
    }
}

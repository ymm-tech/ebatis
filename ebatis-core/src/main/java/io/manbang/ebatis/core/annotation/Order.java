package io.manbang.ebatis.core.annotation;

import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.InternalOrder;
import org.elasticsearch.search.aggregations.KeyComparable;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;

import java.util.Comparator;

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
     * key升序
     */
    TERM_ASC(new InternalOrder((byte) 5, "_term", true, comparingKeys())),
    /**
     * key降序
     */
    TERM_DESC(new InternalOrder((byte) 6, "_term", true, comparingKeys()));

    private final BucketOrder order;

    Order(BucketOrder order) {
        this.order = order;
    }

    public BucketOrder order() {
        return this.order;
    }

    private static Comparator<MultiBucketsAggregation.Bucket> comparingKeys() {
        return (b1, b2) -> {
            if (b1 instanceof KeyComparable) {
                return ((KeyComparable) b1).compareKey(b2);
            }
            throw new IllegalStateException("Unexpected order bucket class [" + b1.getClass() + "]");
        };
    }
}

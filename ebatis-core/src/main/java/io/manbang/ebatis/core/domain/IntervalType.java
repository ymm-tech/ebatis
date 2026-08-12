package io.manbang.ebatis.core.domain;

import org.elasticsearch.index.query.RangeQueryBuilder;

enum IntervalType {
    /**
     * 闭区间
     */
    CLOSED_INTERVAL {
        @Override
        public <T> void left(RangeQueryBuilder builder, T min) {
            builder.gte(min);
        }

        @Override
        public <T> void right(RangeQueryBuilder builder, T max) {
            builder.lte(max);
        }
    },
    /**
     * 开区间
     */
    OPEN_INTERVAL {
        @Override
        public <T> void left(RangeQueryBuilder builder, T min) {
            builder.gt(min);
        }

        @Override
        public <T> void right(RangeQueryBuilder builder, T max) {
            builder.lt(max);
        }
    };

    public abstract <T> void left(RangeQueryBuilder builder, T min);

    public abstract <T> void right(RangeQueryBuilder builder, T max);
}

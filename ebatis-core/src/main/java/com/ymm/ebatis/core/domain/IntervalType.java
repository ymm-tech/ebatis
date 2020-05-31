package com.ymm.ebatis.core.domain;

import org.elasticsearch.index.query.RangeQueryBuilder;

public enum IntervalType {
    CLOSED_INTERVAL {
        @Override
        public <T> void left(RangeQueryBuilder builder, T min) {
            builder.gte(min);
        }

        @Override
        public <T> void right(RangeQueryBuilder builder, T max) {
            builder.lte(max);
        }
    }, OPEN_INTERVAL {
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

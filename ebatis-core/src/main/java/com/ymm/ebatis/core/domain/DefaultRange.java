package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.exception.ConditionNotSupportException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.util.Calendar;
import java.util.Date;


/**
 * 抽象范围类型
 *
 * @param <T> 扩展数据类型
 */
class DefaultRange<T extends Comparable<T>> implements Range<T> {
    private final T min;
    private final T max;
    private IntervalType leftIntervalType;
    private IntervalType rightIntervalType;
    private String name;

    public DefaultRange(T min, T max) {
        this.min = min;
        this.max = max;

        this.openLeft();
        this.openRight();

        if (min == null && max == null) {
            throw new ConditionNotSupportException("范围的上下限不能同时为空");
        }
    }

    @Override
    public Range<T> setName(String name) {
        this.name = name;
        return this;
    }

    private Object getValue(Object value) {
        if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        } else {
            return value;
        }
    }

    @Override
    public Range<T> closeLeft() {
        leftIntervalType = IntervalType.CLOSED_INTERVAL;
        return this;
    }

    @Override
    public Range<T> openLeft() {
        leftIntervalType = IntervalType.OPEN_INTERVAL;
        return this;
    }

    @Override
    public Range<T> closeRight() {
        rightIntervalType = IntervalType.CLOSED_INTERVAL;
        return this;
    }

    @Override
    public Range<T> openRight() {
        rightIntervalType = IntervalType.OPEN_INTERVAL;
        return this;
    }

    @Override
    public QueryBuilder toBuilder() {
        Object left = getValue(min);
        Object right = getValue(max);

        RangeQueryBuilder builder = QueryBuilders.rangeQuery(name);

        // 如果左界限为空，右界限肯定不是空值，因为上面已经判断了，左右界限同时为空的场景
        if (left == null) {
            rightIntervalType.right(builder, right);
        } else if (right == null) {
            leftIntervalType.left(builder, left);
        } else {
            rightIntervalType.right(builder, right);
            leftIntervalType.left(builder, left);
        }

        return builder;
    }
}

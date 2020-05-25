package com.ymm.ebatis.domain;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.Date;

public interface Range<T, D> {
    static Range<Short, Short> numberRange(Short value) {
        return new ShortRange(value);
    }

    static Range<Short, Short> numberRange(Short min, Short max) {
        return new ShortRange(min, max);
    }

    static Range<Integer, Integer> numberRange(Integer value) {
        return new IntegerRange(value);
    }

    static Range<Integer, Integer> numberRange(Integer min, Integer max) {
        return new IntegerRange(min, max);
    }

    static Range<Long, Long> numberRange(Long value) {
        return new LongRange(value);
    }

    static Range<Long, Long> numberRange(Long min, Long max) {
        return new LongRange(min, max);
    }

    static Range<Float, Float> numberRange(Float value) {
        return new FloatRange(value);
    }

    static Range<Float, Float> numberRange(Float min, Float max) {
        return new FloatRange(min, max);
    }

    static Range<Double, Double> numberRange(Double value) {
        return new DoubleRange(value);
    }

    static Range<Double, Double> numberRange(Double min, Double max) {
        return new DoubleRange(min, max);
    }

    static Range<Date, Long> dateRange(Date value) {
        return new DateRange(value);
    }

    static Range<Date, Long> dateRange(Date min, Date max) {
        return new DateRange(min, max);
    }

    Range<T, D> setName(String name);

    Range<T, D> setExcludedMiddle(boolean excludedMiddle);

    Range<T, D> expand(D delta);

    Range<T, D> expand(D leftDelta, D rightDelta);

    IntervalType getLeftIntervalType();

    void setLeftIntervalType(IntervalType type);

    IntervalType getRightIntervalType();

    void setRightIntervalType(IntervalType type);

    QueryBuilder toBuilder();
}

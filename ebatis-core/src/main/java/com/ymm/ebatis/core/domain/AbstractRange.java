package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.exception.ConditionNotSupportException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.util.Objects;


/**
 * 抽象范围类型
 *
 * @param <T> 扩展数据类型
 * @param <D> 扩展增量类型
 */
abstract class AbstractRange<T, D> implements Range<T, D> {
    private IntervalType leftIntervalType = IntervalType.OPEN_INTERVAL;
    private IntervalType rightIntervalType = IntervalType.CLOSED_INTERVAL;
    private boolean excludedMiddle;
    private T value;
    private String name;
    private T min;
    private T max;

    public AbstractRange(T value) {
        this.value = Objects.requireNonNull(value);
        this.excludedMiddle = true;
    }

    public AbstractRange(T min, T max) {
        this.min = min;
        this.max = max;

        if (min == null && max == null) {
            throw new ConditionNotSupportException("范围的上下限不能通为空");
        }

        this.excludedMiddle = false;
    }

    @Override
    public Range<T, D> setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Range<T, D> expand(D delta) {
        return expand(delta, delta);
    }

    @Override
    public Range<T, D> expand(D leftDelta, D rightDelta) {
        if (getValue() == null) {
            return this;
        }

        doExpand(leftDelta, rightDelta);
        return this;
    }

    /**
     * 扩展范围
     *
     * @param leftDelta  左边界增量
     * @param rightDelta 右边界增量
     */
    protected abstract void doExpand(D leftDelta, D rightDelta);

    protected T getValue() {
        return value;
    }

    @Override
    public QueryBuilder toBuilder() {
        Object left = DslUtils.dateToMillis(getMin());
        Object right = DslUtils.dateToMillis(getMax());
        Object middle = DslUtils.dateToMillis(getValue());

        // 左右界限都是空的，说明目前的范围参数是个点，很多情况是这样的，下判断一个点，如果条件不满足，则扩大这个点的范围
        // 比如，先判断等于某个车长的条件，查出来的数据不够数量，则扩大车长范围，并且排除这个车长，作为第二楼层
        if (left == null && right == null) {
            if (middle == null) {
                throw new ConditionNotSupportException();
            }

            return QueryBuilders.termQuery(name, middle);
        }

        RangeQueryBuilder builder = QueryBuilders.rangeQuery(name);

        // 如果左界限为空，右界限肯定不是空值，因为上面已经判断了，左右界限同时为空的场景
        if (left == null) {
            getRightIntervalType().right(builder, right);
        } else if (right == null) {
            getLeftIntervalType().left(builder, left);
        } else {
            getRightIntervalType().right(builder, right);
            getLeftIntervalType().left(builder, left);
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (excludedMiddle && middle != null) {
            boolQueryBuilder.mustNot(new TermQueryBuilder(name, middle));
            boolQueryBuilder.must(builder);

            return boolQueryBuilder;
        } else {
            return builder;
        }
    }

    @Override
    public IntervalType getLeftIntervalType() {
        return leftIntervalType;
    }

    @Override
    public void setLeftIntervalType(IntervalType type) {
        this.leftIntervalType = type;
    }

    @Override
    public IntervalType getRightIntervalType() {
        return rightIntervalType;
    }

    @Override
    public void setRightIntervalType(IntervalType type) {
        this.rightIntervalType = type;
    }

    protected T getMin() {
        return min;
    }

    protected AbstractRange<T, D> setMin(T min) {
        this.min = min;
        return this;
    }

    protected T getMax() {
        return max;
    }

    protected AbstractRange<T, D> setMax(T max) {
        this.max = max;
        return this;
    }

    @Override
    public Range<T, D> setExcludedMiddle(boolean excludedMiddle) {
        this.excludedMiddle = excludedMiddle;
        return this;
    }
}

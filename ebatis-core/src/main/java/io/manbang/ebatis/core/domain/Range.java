package io.manbang.ebatis.core.domain;

import org.elasticsearch.index.query.QueryBuilder;

/**
 * 范围
 *
 * @param <T> 可比较的类型
 */
public interface Range<T extends Comparable<T>> {
    /**
     * 创建指定上下界限的范围，默认左右两边均为开区间
     *
     * @param min 最小值
     * @param max 最大值
     * @param <T> 比较类型发型
     * @return 范围
     */
    static <T extends Comparable<T>> Range<T> of(T min, T max) {
        return new DefaultRange<>(min, max);
    }

    /**
     * 创建小于指定最大值范围
     *
     * @param max 最大值
     * @param <T> 比较类型泛型
     * @return 范围
     */
    static <T extends Comparable<T>> Range<T> lt(T max) {
        return new DefaultRange<>(null, max).openRight();
    }

    /**
     * 创建小于等于指定最大值范围
     *
     * @param max 最大值
     * @param <T> 比较类型泛型
     * @return 范围
     */
    static <T extends Comparable<T>> Range<T> le(T max) {
        return new DefaultRange<>(null, max).closeRight();
    }

    /**
     * 创建大于指定最小值范围
     *
     * @param min 最小值
     * @param <T> 比较类型泛型
     * @return 范围
     */
    static <T extends Comparable<T>> Range<T> gt(T min) {
        return new DefaultRange<>(min, null).openLeft();
    }

    /**
     * 创建大于等于指定最小值范围
     *
     * @param min 最小值
     * @param <T> 比较类型泛型
     * @return 范围
     */
    static <T extends Comparable<T>> Range<T> ge(T min) {
        return new DefaultRange<>(min, null).closeLeft();
    }

    /**
     * 设置字段名称，ES的Mapping
     *
     * @param name 字段名称
     * @return 自身
     */
    Range<T> setName(String name);

    /**
     * 左闭区间
     *
     * @return 自身
     */
    Range<T> closeLeft();

    /**
     * 左开区间
     *
     * @return 自身
     */
    Range<T> openLeft();

    /**
     * 右闭区间
     *
     * @return 自身
     */
    Range<T> closeRight();

    /**
     * 右开区间
     *
     * @return 自身
     */
    Range<T> openRight();

    /**
     * 范围相交
     *
     * @return 自身
     */
    Range<T> intersects();

    /**
     * 范围不相交
     *
     * @return 自身
     */
    Range<T> disjoint();

    /**
     * 范围在内部
     *
     * @return 自身
     */
    Range<T> within();

    /**
     * 范围包含
     *
     * @return 自身
     */
    Range<T> contains();

    /**
     * 转换成ES的插叙构建器
     *
     * @return 查询构建器
     */
    QueryBuilder toBuilder();
}

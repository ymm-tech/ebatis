package com.ymm.ebatis.domain;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * 单页
 *
 * @param <T> 分页实体类型
 * @author 章多亮
 * @since 2019/12/18 9:31
 */
public interface Page<T> extends Iterable<T> {
    /**
     * 创建分页实体
     *
     * @param total    文档总数
     * @param content  分页数据
     * @param pageable 分页信息
     * @param <T>      实体类型
     * @return 分页
     */
    static <T> Page<T> of(long total, List<T> content, Pageable pageable) {
        return new PageImpl<>(total, content, pageable);
    }

    /**
     * 获取分页总数
     *
     * @return 分页总数
     */
    int getTotalPage();

    /**
     * 获取文档总数
     *
     * @return 文档总数
     */
    long getTotal();

    /**
     * 获取分页信息
     *
     * @return 分页信息
     */
    Pageable getPageable();

    /**
     * 获取分页数据
     *
     * @return 分页内容
     */
    List<T> getContent();

    /**
     * 获取页面数据迭代器
     *
     * @return 迭代器
     */
    @Override
    default Iterator<T> iterator() {
        return getContent().iterator();
    }

    /**
     * 判断是否还有下一页数据
     *
     * @return 有下一页，返回<code>true</code>
     */
    default boolean hasNext() {
        return getTotalPage() > getPageable().getPage() + 1;
    }

    /**
     * 获取当前页面数据的数量
     *
     * @return 当前页面的数据数量
     */
    default int getTotalElement() {
        return getContent().size();
    }


    /**
     * 判断当前页是否有数据
     *
     * @return 如果当前页没有数据，返回<code>true</code>
     */
    default boolean isEmpty() {
        return getContent().isEmpty();
    }

    /**
     * 获取分页数据流
     *
     * @return 分页数据串行流
     */
    default Stream<T> stream() {
        return getContent().stream();
    }

    /**
     * 获取分页数据并行流
     *
     * @return 分页数据并行流
     */
    default Stream<T> parallelStream() {
        return getContent().parallelStream();
    }
}

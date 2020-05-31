package com.ymm.ebatis.core.domain;

/**
 * 分页对象，页面从0开始算第一页。
 *
 * @author 章多亮
 * @since 2019/12/18 16:12
 */
public interface Pageable {
    /**
     * 创建分页信息
     *
     * @param page 页码，从0开始
     * @param size 页面大小
     * @return 分页信息
     */
    static Pageable of(int page, int size) {
        return of(page, size, 0);
    }

    static Pageable first(int size) {
        return of(0, size);
    }

    /**
     * 创建分页信息
     *
     * @param page   页码，从0开始
     * @param size   页面大小
     * @param offset 偏移量
     * @return 分页信息
     */
    static Pageable of(int page, int size, int offset) {
        return new PageRequest(page, size, offset);
    }

    /**
     * 创建分页信息
     *
     * @param size   页面大小
     * @param offset 偏移量
     * @return 分页信息
     */
    static Pageable firstWithOffset(int size, int offset) {
        return of(0, size, offset);
    }

    /**
     * 获取页码，从0开始
     *
     * @return 页面
     */
    int getPage();

    /**
     * 获取分页大小，默认20
     *
     * @return 分页大小
     */
    int getSize();

    /**
     * 偏移量大小，默认0
     *
     * @return 偏移量大大小
     */
    int getOffset();

    /**
     * 获取文档偏移量
     *
     * @return 偏移量
     */
    default int getFrom() {
        return getPage() * getSize() + getOffset();
    }
}

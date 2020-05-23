package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.annotation.Ignore;
import lombok.ToString;

/**
 * 分页请求，
 *
 * @author 章多亮
 * @since 2020/05/21 10:58:13
 */
@ToString
public class PageRequest implements Pageable {
    /**
     * 页码，从0开始
     */
    @Ignore
    private int page;
    /**
     * 分页大小，默认20
     */
    @Ignore
    private int size;
    /**
     * 页面偏移量，即忽略多少条数据后，开始分页
     */
    @Ignore
    private int offset;

    public PageRequest() {
        this(0, 20, 0);
    }

    public PageRequest(int page, int size) {
        this(page, size, 0);
    }

    public PageRequest(int page, int size, int offset) {
        this.page = page;
        this.size = size;
        this.offset = offset;
    }

    @Override
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}

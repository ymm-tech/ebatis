package io.manbang.ebatis.core.domain;

import io.manbang.ebatis.core.annotation.Ignore;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页请求，
 *
 * @author 章多亮
 * @since 2020/05/21 10:58:13
 */
@Setter
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

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Pageable next() {
        return Pageable.withOffset(page + 1, size, offset);
    }

    @Override
    public Pageable previous() {
        return page == 0 ? Pageable.firstWithOffset(size, offset) : Pageable.withOffset(page - 1, size, offset);
    }
}

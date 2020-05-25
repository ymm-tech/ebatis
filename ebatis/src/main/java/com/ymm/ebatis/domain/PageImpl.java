package com.ymm.ebatis.domain;

import java.util.Collections;
import java.util.List;

/**
 * 页面信息实现
 *
 * @author 章多亮
 * @since 2019/12/18 16:12
 */
public class PageImpl<T> implements Page<T> {
    private final long total;
    private final List<T> content;
    private final Pageable pageable;
    private final int totalPage;

    public PageImpl(long total, List<T> content, Pageable pageable) {
        this.total = total;
        this.content = Collections.unmodifiableList(content);
        this.pageable = pageable;
        this.totalPage = total % pageable.getSize() == 0 ? (int) (total / pageable.getSize()) : (int) (total / pageable.getSize() + 1);
    }

    @Override
    public int getTotalPage() {
        return totalPage;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public List<T> getContent() {
        return content;
    }
}

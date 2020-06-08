package com.ymm.ebatis.core.domain;

import lombok.ToString;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/6/8 13:55
 */
@ToString(of = "scrollId")
class DefaultScrollResponse<T> implements ScrollResponse<T> {
    private final String scrollId;
    private final Page<T> response;

    DefaultScrollResponse(String scrollId, Page<T> response) {
        this.scrollId = scrollId;
        this.response = response;
    }

    @Override
    public String getScrollId() {
        return scrollId;
    }

    @Override
    public Page<T> getResponse() {
        return response;
    }

    @Override
    public Stream<T> stream() {
        return response.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return response.parallelStream();
    }

    @Override
    public boolean isEmpty() {
        return response.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return response.iterator();
    }
}

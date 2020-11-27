package io.manbang.ebatis.core.domain;

import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/5/25 15:47
 */
public interface ScrollResponse<T> extends Iterable<T> {

    /**
     * create scroll response
     *
     * @param scrollId search scroll id
     * @param response documents page
     * @param <T>      result type
     * @return Scroll Response
     */
    static <T> ScrollResponse<T> of(String scrollId, Page<T> response) {
        return new DefaultScrollResponse<>(scrollId, response);
    }

    /**
     * Elasticsearch returns another batch of results with a new scroll identifier.
     * This new scroll identifier can then be used in a subsequent SearchScrollRequest to retrieve the next batch of results, and so on.
     * This process should be repeated in a loop until no more results are returned, meaning that the scroll has been exhausted and all the matching documents have been retrieved.
     *
     * @return scroll id
     */
    String getScrollId();

    /**
     * 获取当前批次数据分页
     *
     * @return one batch of results
     */
    Page<T> getResponse();

    Stream<T> stream();

    Stream<T> parallelStream();

    boolean isEmpty();
}

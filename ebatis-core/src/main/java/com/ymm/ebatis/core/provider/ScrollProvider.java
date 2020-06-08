package com.ymm.ebatis.core.provider;

/**
 * Scroll Id Provider, Search Scroll condition must implement this interface.
 * If return
 *
 * @author 章多亮
 * @since 2020/6/8 9:27
 */
public interface ScrollProvider extends Provider {
    /**
     * determine whether current request is clear scroll request; if return <code>true</code>, {@link ScrollProvider#getScrollId()} must return a none null value.
     *
     * @return if want to clear scroll, return <code>true</code>
     */
    default boolean isClearScroll() {
        return false;
    }

    /**
     * fetch search scroll id, if the return value is null, it means that current request is initial search request.
     *
     * @return scroll id
     */
    String getScrollId();
}

package com.ymm.ebatis.domain;

/**
 * @author 章多亮
 * @since 2020/5/25 15:47
 */
public interface ScrollResponse {
    /**
     * 设置游标Id
     *
     * @param scrollId Search Scroll Id
     */
    void setScrollId(String scrollId);
}

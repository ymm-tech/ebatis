package com.ymm.ebatis.domain;

import org.elasticsearch.search.sort.ScriptSortBuilder;

/**
 * @author 章多亮
 * @since 2020/1/6 19:43
 */
public interface ScriptSort extends Sort {
    /**
     * 获取排序脚本
     *
     * @return 排序脚本
     */
    Script script();

    /**
     * 脚本排序类型
     *
     * @return 排序类型
     */
    ScriptSortBuilder.ScriptSortType sortType();
}

package com.ymm.ebatis.domain;

import org.elasticsearch.search.sort.ScriptSortBuilder;

/**
 * @author 章多亮
 * @since 2019/12/26 10:24
 */
public interface SortScript {
    /**
     * 创建数值类型排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static SortScript number(Script script) {
        return new TypedSortScript(script, ScriptSortBuilder.ScriptSortType.NUMBER);
    }

    /**
     * 创建字符串类型排序脚本
     *
     * @param script 脚本
     * @return 排序脚本
     */
    static SortScript string(Script script) {
        return new TypedSortScript(script, ScriptSortBuilder.ScriptSortType.STRING);
    }

    /**
     * 脚本排序类型
     *
     * @return 类型
     */
    ScriptSortBuilder.ScriptSortType getType();

    /**
     * 获取排序脚本
     *
     * @return 脚本
     */
    Script getScript();
}

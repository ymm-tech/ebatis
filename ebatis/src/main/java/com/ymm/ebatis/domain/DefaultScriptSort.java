package com.ymm.ebatis.domain;

import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

/**
 * @author 章多亮
 * @since 2020/1/6 20:03
 */
class DefaultScriptSort extends AbstractSort implements ScriptSort {
    private final Script script;
    private final ScriptSortBuilder.ScriptSortType sortType;
    private final SortDirection sortDirection;

    DefaultScriptSort(Script script, ScriptSortBuilder.ScriptSortType sortType, SortDirection sortDirection) {
        this.script = script;
        this.sortType = sortType;
        this.sortDirection = sortDirection;
    }

    @Override
    public Script script() {
        return script;
    }

    @Override
    public ScriptSortBuilder.ScriptSortType sortType() {
        return sortType;
    }


    @Override
    public SortBuilder<?> toSortBuilder() {
        ScriptSortBuilder builder = SortBuilders.scriptSort(script.toEsScript(), sortType).order(sortDirection.getOrder());
        setSortMode(builder::sortMode);
        return builder;
    }
}

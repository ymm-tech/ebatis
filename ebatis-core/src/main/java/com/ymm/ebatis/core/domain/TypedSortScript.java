package com.ymm.ebatis.core.domain;

import org.elasticsearch.search.sort.ScriptSortBuilder;

/**
 * @author 章多亮
 * @since 2019/12/26 10:27
 */
class TypedSortScript implements SortScript {
    private final Script script;
    private final ScriptSortBuilder.ScriptSortType type;

    TypedSortScript(Script script, ScriptSortBuilder.ScriptSortType type) {
        this.script = script;
        this.type = type;
    }

    @Override
    public ScriptSortBuilder.ScriptSortType getType() {
        return type;
    }

    @Override
    public Script getScript() {
        return script;
    }
}

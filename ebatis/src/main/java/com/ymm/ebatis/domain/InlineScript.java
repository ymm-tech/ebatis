package com.ymm.ebatis.domain;

import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

class InlineScript extends AbstractScript {
    InlineScript(String code, Object params) {
        super(code, params);
    }

    @Override
    public Script toEsScript() {
        return new Script(ScriptType.INLINE, getLang(), getIdOrCode(), getOptions(), getParams());
    }
}

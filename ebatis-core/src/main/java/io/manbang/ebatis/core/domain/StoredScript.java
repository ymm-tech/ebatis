package io.manbang.ebatis.core.domain;

import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

class StoredScript extends AbstractScript {
    StoredScript(String id, Object params) {
        super(id, params);
    }

    @Override
    public Script toEsScript() {
        return new Script(ScriptType.STORED, null, getIdOrCode(), null, getParams());
    }
}

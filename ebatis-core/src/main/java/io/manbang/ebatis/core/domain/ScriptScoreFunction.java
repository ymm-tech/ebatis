package io.manbang.ebatis.core.domain;

import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScriptScoreFunctionBuilder;

class ScriptScoreFunction implements ScoreFunction {
    private final Script script;

    ScriptScoreFunction(Script script) {
        this.script = script;
    }

    public Script getScript() {
        return script;
    }

    public void setParams(Object params) {
        script.setParams(params);
    }

    @Override
    public FunctionScoreQueryBuilder.FilterFunctionBuilder toFunctionBuilder() {
        return new FunctionScoreQueryBuilder.FilterFunctionBuilder(new ScriptScoreFunctionBuilder(script.toEsScript()));
    }
}

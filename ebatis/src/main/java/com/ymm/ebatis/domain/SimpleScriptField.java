package com.ymm.ebatis.domain;

class SimpleScriptField implements ScriptField {
    private final String name;
    private final Script script;
    private boolean ignoreFailure;

    SimpleScriptField(String name, Script script) {
        this.name = name;
        this.script = script;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Script getScript() {
        return script;
    }

    @Override
    public boolean isIgnoreFailure() {
        return ignoreFailure;
    }

    @Override
    public void setIgnoreFailure(boolean ignoreFailure) {
        this.ignoreFailure = ignoreFailure;
    }
}

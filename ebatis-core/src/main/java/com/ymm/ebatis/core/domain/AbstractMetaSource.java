package com.ymm.ebatis.core.domain;

public abstract class AbstractMetaSource implements MetaSource {
    private ResponseMeta responseMeta;

    public ResponseMeta getResponseMeta() {
        return responseMeta;
    }

    @Override
    public void setResponseMeta(ResponseMeta responseMeta) {
        this.responseMeta = responseMeta;
    }
}

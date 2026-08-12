package io.manbang.ebatis.core.domain;

import lombok.Getter;

@Getter
public abstract class AbstractMetaSource implements MetaSource {
    private ResponseMeta responseMeta;

    @Override
    public void setResponseMeta(ResponseMeta responseMeta) {
        this.responseMeta = responseMeta;
    }
}

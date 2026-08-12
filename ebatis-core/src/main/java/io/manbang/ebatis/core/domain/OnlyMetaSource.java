package io.manbang.ebatis.core.domain;

import lombok.Data;

@Data
class OnlyMetaSource implements MetaSource {
    private final ResponseMeta responseMeta;

    static MetaSource only(ResponseMeta meta) {
        return new OnlyMetaSource(meta);
    }

    @Override
    public void setResponseMeta(ResponseMeta responseMeta) {
        throw new UnsupportedOperationException();
    }
}

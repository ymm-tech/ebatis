package io.manbang.ebatis.core.domain;

/**
 * 元信息
 *
 * @author duoliang.zhang
 */
public interface MetaSource {
    static MetaSource only(ResponseMeta meta) {
        return OnlyMetaSource.only(meta);
    }

    /**
     * 设置元信息
     *
     * @param responseMeta 元信息
     */
    void setResponseMeta(ResponseMeta responseMeta);

    default ResponseMeta getResponseMeta() {
        throw new UnsupportedOperationException();
    }
}

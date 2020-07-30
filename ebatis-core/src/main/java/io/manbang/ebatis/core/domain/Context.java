package io.manbang.ebatis.core.domain;


import io.manbang.ebatis.core.response.ResponseExtractor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author duoliang.zhang
 */
public class Context {
    private Pageable pageable;
    private Pageable[] pageables;
    private HttpConfig httpConfig;
    private ResponseExtractor<?> responseExtractor;
    private Map<String, Object> contextMap;

    public Optional<Pageable> getPageable() {
        return Optional.ofNullable(pageable);
    }

    void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Optional<ResponseExtractor<?>> getResponseExtractor() { // NOSONAR
        return Optional.ofNullable(responseExtractor);
    }

    public void setResponseExtractor(ResponseExtractor<?> responseExtractor) {
        this.responseExtractor = responseExtractor;
    }

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    void setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }

    public Optional<Pageable[]> getPageables() {
        return Optional.ofNullable(pageables);
    }

    void setPageables(Pageable[] pageables) {
        this.pageables = pageables;
    }

    void setValue(String key, Object value) {
        if (Objects.isNull(contextMap)) {
            contextMap = new HashMap<>(4);
        }
        contextMap.put(key, value);
    }

    Object getValue(String key) {
        return Optional.ofNullable(contextMap).map(m -> m.get(key)).orElse(null);
    }
}

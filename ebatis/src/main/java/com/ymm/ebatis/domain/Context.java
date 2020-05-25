package com.ymm.ebatis.domain;


import com.ymm.ebatis.response.ResponseExtractor;
import org.springframework.util.StopWatch;

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
    private StopWatch stopWatch;
    private ResponseExtractor<?> responseExtractor;
    private Map<String, Object> contextMap;

    public Optional<Pageable> getPageable() {
        return Optional.ofNullable(pageable);
    }

    void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Optional<StopWatch> getStopWatch() {
        return Optional.ofNullable(stopWatch);
    }

    public void setStopWatch(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public Optional<ResponseExtractor<?>> getResponseExtractor() {
        return Optional.ofNullable(responseExtractor);
    }

    public void setResponseExtractor(ResponseExtractor<?> responseExtractor) {
        this.responseExtractor = responseExtractor;
    }

    public Optional<Pageable[]> getPageables() {
        return Optional.ofNullable(pageables);
    }

    void setPageables(Pageable[] pageables) {
        this.pageables = pageables;
    }

    void setValue(String key, Object o) {
        if (Objects.isNull(contextMap)) {
            contextMap = new HashMap<>();
        }
        contextMap.put(key, o);
    }

    Object getValue(String key) {
        return Optional.ofNullable(contextMap).map(m -> m.get(key)).orElse(null);
    }
}

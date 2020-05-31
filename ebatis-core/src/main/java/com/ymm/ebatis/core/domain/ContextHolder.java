package com.ymm.ebatis.core.domain;

import com.ymm.ebatis.core.response.ResponseExtractor;

import java.util.Optional;

/**
 * @author duoliang.zhang
 */
public class ContextHolder {
    private static final ThreadLocal<Context> CONTEXT_THREAD_LOCAL = ThreadLocal.withInitial(Context::new);

    private ContextHolder() {
        throw new UnsupportedOperationException();
    }

    public static Context getContext() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void setContext(Context context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }

    public static Optional<ResponseExtractor<?>> getResponseExtractor() {
        return getContext().getResponseExtractor();
    }

    public static void setResponseExtractor(ResponseExtractor<?> responseExtractor) {
        getContext().setResponseExtractor(responseExtractor);
    }


    static Optional<Pageable[]> getPageables() {
        return getContext().getPageables();
    }

    public static void setPageables(Pageable[] pageables) {
        getContext().setPageables(pageables);
    }


    static Optional<Pageable> getPageable() {
        return getContext().getPageable();
    }

    public static void setPageable(Pageable pageable) {
        getContext().setPageable(pageable);
    }

    public static void remove() {
        CONTEXT_THREAD_LOCAL.remove();
    }

    public static void setValue(String key, Object o) {
        getContext().setValue(key, o);
    }

    public static String getString(String key) {
        return getValue(key);
    }

    public static Boolean getBoolean(String key) {
        return getValue(key);
    }

    public static Integer getInteger(String key) {
        return getValue(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String key) {
        return (T) getContext().getValue(key);
    }
}

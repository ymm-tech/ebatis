package com.ymm.ebatis.core.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 懒加载容器
 *
 * @author duoliang.zhang
 */
class LazyHolder {
    private static final Map<Supplier<?>, Object> VALUES = new ConcurrentHashMap<>();

    private LazyHolder() {
    }

    @SuppressWarnings("unchecked")
    static <T> T valueOf(Supplier<T> supplier) {
        return (T) VALUES.computeIfAbsent(supplier, Supplier::get);
    }
}

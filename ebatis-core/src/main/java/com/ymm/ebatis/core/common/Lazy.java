package com.ymm.ebatis.core.common;

import java.util.function.Supplier;

/**
 * 延迟对象
 *
 * @param <T> 延迟初始化的类型
 * @author duoliang.zhang
 */
public final class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return LazyHolder.valueOf(supplier);
    }
}

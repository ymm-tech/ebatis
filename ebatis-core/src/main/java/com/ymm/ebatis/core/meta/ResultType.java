package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.domain.Page;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author duoliang.zhang
 */

@Slf4j
public enum ResultType {
    /**
     * {@link CompletableFuture}
     */
    COMPLETABLE_FUTURE(CompletableFuture.class) {
        @Override
        public <T> Object adaptResult(CompletableFuture<T> futureResult) {
            return futureResult;
        }
    },
    /**
     * {@link Optional}
     */
    OPTIONAL(Optional.class) {
        @Override
        public <T> Optional<Object> adaptResult(CompletableFuture<T> futureResult) {
            return Optional.ofNullable(futureResult.join());
        }
    },
    /**
     * {@link Page}
     */
    PAGE(Page.class) {
        @Override
        public <T> Object adaptResult(CompletableFuture<T> futureResult) {
            return futureResult.join();
        }
    },
    /**
     * {@link List}
     */
    LIST(List.class) {
        @Override
        public <T> Object adaptResult(CompletableFuture<T> futureResult) {
            return futureResult.join();
        }
    },
    /**
     * 其他实体类型
     */
    OTHER(Object.class) {
        @Override
        public <T> Object adaptResult(CompletableFuture<T> futureResult) {
            return futureResult.join();
        }
    };

    private static final Map<Class<?>, ResultType> METHOD_RETURN_TYPES = new HashMap<>();

    static {
        for (ResultType type : values()) {
            METHOD_RETURN_TYPES.put(type.returnType, type);
        }
    }

    private final Class<?> returnType;

    ResultType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public static ResultType valueOf(Method method) {
        return METHOD_RETURN_TYPES.getOrDefault(method.getReturnType(), OTHER);
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    /**
     * 适配实际的接口返回值
     *
     * @param futureResult 异步结果
     * @param <T>          异步结果类型
     * @return 适配方法的返回值类型
     */
    public abstract <T> Object adaptResult(CompletableFuture<T> futureResult);

}

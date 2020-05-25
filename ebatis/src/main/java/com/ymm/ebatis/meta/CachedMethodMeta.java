package com.ymm.ebatis.meta;

import com.ymm.ebatis.exception.RequestFactoryNotFoundException;
import com.ymm.ebatis.request.RequestFactoryType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 章多亮
 * @since 2019/12/17 15:03
 */
@ToString(of = "method")
@EqualsAndHashCode(of = "method")
class CachedMethodMeta implements MethodMeta {
    private static final Map<Method, MethodMeta> METHOD_METAS = new ConcurrentHashMap<>();
    private final Method method;
    private final RequestFactoryType requestFactoryType;
    private final ParameterMeta[] parameterMetas;

    private CachedMethodMeta(Method method) {
        this.method = method;
        this.requestFactoryType = getRequestFactoryType(method);
        this.parameterMetas = getParameterMetas(method);
    }

    static MethodMeta of(Method method) {
        return METHOD_METAS.computeIfAbsent(method, CachedMethodMeta::new);
    }

    private RequestFactoryType getRequestFactoryType(Method method) {
        return RequestFactoryType.from(method).orElseThrow(() -> new RequestFactoryNotFoundException(method.toString()));
    }

    private ParameterMeta[] getParameterMetas(Method method) {
        ParameterMeta[] metas = new ParameterMeta[method.getParameterCount()];

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            metas[i] = ParameterMeta.withIndex(i, parameters[i]);
        }
        return metas;
    }

    @Override
    public RequestFactoryType getRequestFactoryType() {
        return requestFactoryType;
    }

    @Override
    public ParameterMeta[] getParameterMetas() {
        return parameterMetas;
    }

    @Override
    public Method getMethod() {
        return method;
    }
}

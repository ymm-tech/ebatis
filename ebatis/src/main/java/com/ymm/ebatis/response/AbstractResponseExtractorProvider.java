package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/17 14:12
 */
public abstract class AbstractResponseExtractorProvider implements ResponseExtractorProvider {
    private final RequestType requestType;
    private final ResultType[] resultTypes;

    protected AbstractResponseExtractorProvider(RequestType requestType, ResultType... resultTypes) {
        this.requestType = requestType;
        this.resultTypes = resultTypes;
    }

    @Override
    public boolean support(MethodMeta method) {
        ResultType resultType = method.getResultType();
        return this.requestType == method.getRequestType() && ArrayUtils.contains(this.resultTypes, resultType);
    }

    @Override
    public ResponseExtractor<?> getResponseExtractor(MethodMeta method) {
        ResolvableType resolvedResultType;
        if (isWrapped()) {
            resolvedResultType = ResolvableType.forMethodReturnType(method.getMethod()).getGeneric(0);
        } else {
            resolvedResultType = ResolvableType.forMethodReturnType(method.getMethod());
        }

        return getResponseExtractor(resolvedResultType);
    }

    protected abstract ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType);

    /**
     * 获取去掉了 {@link java.util.concurrent.CompletableFuture} 和 {@link java.util.Optional} 类型
     */
    protected abstract boolean isWrapped();
}

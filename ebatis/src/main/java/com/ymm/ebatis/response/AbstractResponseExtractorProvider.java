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
    public ResponseExtractor<?> getResponseExtractor(MethodMeta meta) {
        ResolvableType resolvedResultType;
        if (isWrapped()) {
            resolvedResultType = ResolvableType.forMethodReturnType(meta.getElement()).getGeneric(0);
        } else {
            resolvedResultType = ResolvableType.forMethodReturnType(meta.getElement());
        }

        return getResponseExtractor(resolvedResultType);
    }

    /**
     * 获取指定类型返回值提取器
     *
     * @param resolvedResultType 解析后的返回值类型，已经却掉包装类型
     * @return 响应提取器
     */
    protected abstract ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType); // NOSONAR

    /**
     * 判断返回值是否被 {@link java.util.concurrent.CompletableFuture} 和 {@link java.util.Optional} 类型包装
     *
     * @return 如果返回值类型的是 {@link java.util.concurrent.CompletableFuture} 和 {@link java.util.Optional}，返回<code>true</code>
     */
    protected abstract boolean isWrapped();
}

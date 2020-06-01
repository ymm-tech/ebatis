package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Http;
import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.domain.ContextHolder;
import com.ymm.ebatis.core.domain.HttpConfig;
import com.ymm.ebatis.core.exception.RequestTypeNotSupportException;
import com.ymm.ebatis.core.executor.RequestExecutor;
import com.ymm.ebatis.core.response.ResponseExtractor;
import com.ymm.ebatis.core.response.ResponseExtractorLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/27 18:27
 */
@Slf4j
public class DefaultMapperMethodMeta implements MapperMethod {
    private final Method method;
    private final List<ParameterMeta> parameterMetas;

    private final String[] indices;
    private final String[] types;
    private final String routing;
    private final RequestType requestType;
    private final ResultType resultType;
    private final RequestExecutor requestExecutor;
    private final Annotation requestAnnotation;
    private final HttpConfig httpConfig;
    private ParameterMeta conditionParameter;
    private ParameterMeta pageableParameter;
    private ParameterMeta responseExtractorParameter;

    public DefaultMapperMethodMeta(MapperInterface mapperInterface, Method method) {
        this.method = method;

        this.indices = mapperInterface.getIndices();
        this.types = mapperInterface.getTypes();
        this.routing = mapperInterface.getRouting();

        this.requestType = getRequestType(method);
        this.resultType = getResultType(method);

        this.requestAnnotation = getAnnotation(requestType.getAnnotationClass());
        this.httpConfig = getHttpConfig(mapperInterface);

        this.requestExecutor = requestType.getRequestExecutor();
        this.parameterMetas = getParameterMetas(method);
    }

    private HttpConfig getHttpConfig(MapperInterface mapperInterface) {
        Http http = findAnnotation(Http.class).orElse(null);

        if (http == null) {
            return mapperInterface.getHttpConfig();
        }

        return new HttpConfig()
                .connectionRequestTimeout(http.connectionRequestTimeout())
                .connectTimeout(http.connectTimeout())
                .socketTimeout(http.socketTimeout());

    }

    private List<ParameterMeta> getParameterMetas(Method method) {
        List<ParameterMeta> metas = new ArrayList<>(method.getParameterCount());
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            ParameterMeta meta = ParameterMeta.withIndex(this, parameters[i], i);
            metas.add(meta);

            if (meta.isPageable()) {
                this.pageableParameter = meta;
            } else if (meta.isResponseExtractor()) {
                this.responseExtractorParameter = meta;
            } else {
                this.conditionParameter = meta;
            }
        }

        return Collections.unmodifiableList(metas);
    }

    private ResultType getResultType(Method method) {
        return ResultType.valueOf(method);
    }

    private RequestType getRequestType(Method method) {
        return RequestType.valueOf(method).orElseThrow(() -> new RequestTypeNotSupportException(method.toString()));
    }

    @Override
    public Object invoke(Cluster cluster, Object[] args) {
        try {
            return requestExecutor.execute(cluster, this, args);
        } catch (Exception e) {
            log.error("接口执行异常: {}", method, e);
            throw e;
        } finally {
            ContextHolder.remove();
        }
    }

    @Override
    public String[] getIndices() {
        return indices;
    }

    @Override
    public String[] getTypes() {
        return types;
    }

    @Override
    public String getRouting() {
        return routing;
    }

    @Override
    public RequestType getRequestType() {
        return requestType;
    }

    @Override
    public ResultType getResultType() {
        return resultType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getRequestAnnotation() {
        return (A) requestAnnotation;
    }

    @Override
    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    @Override
    public List<ParameterMeta> getParameterMetas() {
        return parameterMetas;
    }

    @Override
    public ParameterMeta getConditionParameter() {
        return conditionParameter;
    }

    @Override
    public Optional<ParameterMeta> findConditionParameter() {
        return Optional.ofNullable(conditionParameter);
    }

    @Override
    public Optional<ParameterMeta> getPageableParameter() {
        return Optional.ofNullable(pageableParameter);
    }

    @Override
    public ParameterMeta getResponseExtractorParameter() {
        return responseExtractorParameter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResponseExtractor<T> getResponseExtractor(Object[] args) {
        return responseExtractorParameter == null ? ResponseExtractorLoader.getResponseExtractor(this) : (ResponseExtractor<T>) responseExtractorParameter.getValue(args);
    }

    @Override
    public Method getElement() {
        return method;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}

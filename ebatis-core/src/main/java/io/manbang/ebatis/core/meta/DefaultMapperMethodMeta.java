package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.annotation.Http;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.domain.HttpConfig;
import io.manbang.ebatis.core.exception.RequestTypeNotSupportException;
import io.manbang.ebatis.core.executor.RequestExecutor;
import io.manbang.ebatis.core.mapper.MappingRouter;
import io.manbang.ebatis.core.response.ResponseExtractor;
import io.manbang.ebatis.core.response.ResponseExtractorLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author 章多亮
 * @since 2020/5/27 18:27
 */
@Slf4j
class DefaultMapperMethodMeta implements MapperMethod {
    private final Method method;
    private final List<ParameterMeta> parameterMetas;

    private final String[] indices;
    private final String[] types;
    private final BiFunction<MethodMeta, Object[], String[]> indicesSupplier;
    private final BiFunction<MethodMeta, Object[], String[]> typesSupplier;
    private final RequestType requestType;
    private final ResultType resultType;
    private final RequestExecutor requestExecutor;
    private final Annotation requestAnnotation;
    private final HttpConfig httpConfig;
    private final String[] includeFields;
    private final Class<?> returnType;
    private final Class<?> unwrappedReturnType;
    private ParameterMeta conditionParameter;
    private ParameterMeta pageableParameter;
    private ParameterMeta responseExtractorParameter;

    DefaultMapperMethodMeta(MapperInterface mapperInterface, Method method) {
        this.method = method;
        this.returnType = method.getReturnType();

        this.indices = mapperInterface.getIndices();
        this.types = mapperInterface.getTypes();
        final Optional<MappingRouter> mappingRouter = Optional.ofNullable(mapperInterface.getMappingRouter());
        this.indicesSupplier = mappingRouter.map(r -> (BiFunction<MethodMeta, Object[], String[]>) r::indices)
                .orElse((methodMeta, objects) -> indices);
        this.typesSupplier = mappingRouter.map(r -> (BiFunction<MethodMeta, Object[], String[]>) r::types)
                .orElse((methodMeta, objects) -> types);

        this.requestType = getRequestType(method);
        this.resultType = getResultType(method);

        this.requestAnnotation = getAnnotation(requestType.getAnnotationClass());
        this.httpConfig = getHttpConfig(mapperInterface);

        this.requestExecutor = requestType.getRequestExecutor();
        this.parameterMetas = getParameterMetas(method);

        this.unwrappedReturnType = requestType.getEntityClass(this).orElse(null);
        this.includeFields = getIncludeFields(unwrappedReturnType);

        validate();
    }

    private void validate() {
        boolean validated = this.requestType.validate(this);
        if (!validated) {
            throw new UnsupportedOperationException(this.toString());
        }
    }

    private String[] getIncludeFields(Class<?> clazz) {
        return Optional.ofNullable(clazz)
                .filter(c -> !MetaUtils.isBasic(c))
                .map(ClassMeta::of)
                .map(c -> c.getFieldMetas().stream().map(FieldMeta::getName).toArray(String[]::new))
                .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    private HttpConfig getHttpConfig(MapperInterface mapperInterface) {
        return findAnnotation(Http.class)
                .map(this::createHttpConfig)
                .orElse(mapperInterface.getHttpConfig());
    }

    private HttpConfig createHttpConfig(Http http) {
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
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public String[] getIndices(MethodMeta meta, Object[] args) {
        return indicesSupplier.apply(meta, args);
    }

    @Override
    public String[] getTypes(MethodMeta meta, Object[] args) {
        return typesSupplier.apply(meta, args);
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
    public String[] getIncludeFields() {
        return includeFields;
    }

    @Override
    public Optional<Class<?>> unwrappedReturnType() {
        return Optional.ofNullable(unwrappedReturnType);
    }

    @Override
    public ResponseExtractor<?> getResponseExtractor(Object[] args) {
        return responseExtractorParameter == null ? ResponseExtractorLoader.getResponseExtractor(this) : (ResponseExtractor<?>) responseExtractorParameter.getValue(args);
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

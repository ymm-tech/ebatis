package io.manbang.ebatis.core.proxy;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.cluster.ClusterRouter;
import io.manbang.ebatis.core.domain.Context;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.domain.HttpConfig;
import io.manbang.ebatis.core.exception.EbatisException;
import io.manbang.ebatis.core.interceptor.DefaultPostResponseInfo;
import io.manbang.ebatis.core.interceptor.DefaultPreResponseInfo;
import io.manbang.ebatis.core.interceptor.DefaultRequestInfo;
import io.manbang.ebatis.core.interceptor.Interceptor;
import io.manbang.ebatis.core.interceptor.InterceptorFactory;
import io.manbang.ebatis.core.mapper.IndexApi;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.ParameterMeta;
import io.manbang.ebatis.core.meta.RequestType;
import io.manbang.ebatis.core.meta.ResultType;
import io.manbang.ebatis.core.response.ResponseExtractor;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author weilong.hu
 * @since 2021/07/05 15:29
 */
class IndexApiImpl implements IndexApi {
    private final static Interceptor INTERCEPTOR = InterceptorFactory.interceptors();

    static {
        try {
            //兼容低版本集群
            final Field fieldPatser = CreateIndexResponse.class.getDeclaredField("PARSER");
            fieldPatser.setAccessible(true);
            final ConstructingObjectParser<CreateIndexResponse, Void> PARSER = (ConstructingObjectParser<CreateIndexResponse, Void>) fieldPatser.get(null);
            final Field constructingObjectParserField = ConstructingObjectParser.class.getDeclaredField("constructorArgInfos");
            constructingObjectParserField.setAccessible(true);
            final List<Object> constructorArgInfos = (List<Object>) constructingObjectParserField.get(PARSER);
            final Object constructorArgInfo = constructorArgInfos.get(2);
            final Field requiredField = constructorArgInfo.getClass().getDeclaredField("required");
            requiredField.setAccessible(true);
            requiredField.set(constructorArgInfo, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("IndexApiImpl init fail...", e);
        }
    }

    private final LazyInitializer<ClusterRouter> clusterRouter;

    IndexApiImpl(LazyInitializer<ClusterRouter> clusterRouter) {
        this.clusterRouter = clusterRouter;
    }

    public static IndexApi index(LazyInitializer<ClusterRouter> clusterRouter) {
        return new IndexApiImpl(clusterRouter);
    }

    @Override
    public RefreshResponse refresh(String... indices) {
        return refreshAsync(indices).join();
    }

    @Override
    public CompletableFuture<RefreshResponse> refreshAsync(String... indices) {
        CompletableFuture<RefreshResponse> future = new CompletableFuture<>();
        Context context = ContextHolder.getContext();
        final RefreshRequest refreshRequest = Requests.refreshRequest(indices);
        try {
            final Method createAsync = this.getClass().getDeclaredMethod("refreshAsync", String[].class);
            final MethodMeta methodMeta = getMethodMeta(Objects.isNull(indices) ? new String[]{"_all"} : indices, createAsync);
            final Object[] args = {indices};
            final Cluster cluster = clusterRouter.get().route(methodMeta);
            INTERCEPTOR.preRequest(args, cluster, methodMeta);
            INTERCEPTOR.postRequest(new DefaultRequestInfo<>(refreshRequest, args));
            INTERCEPTOR.preResponse(new DefaultPreResponseInfo<>(refreshRequest, null));
            cluster.highLevelClient().indices().refreshAsync(refreshRequest, RequestOptions.DEFAULT, ActionListener.wrap(response -> {
                ContextHolder.setContext(context);
                try {
                    future.complete(response);
                    INTERCEPTOR.postResponse(new DefaultPostResponseInfo<>(refreshRequest, response));
                } finally {
                    ContextHolder.remove();
                }
            }, exception -> {
                future.completeExceptionally(exception);
                INTERCEPTOR.handleException(exception);
                ContextHolder.remove();
            }));
        } catch (ConcurrentException | NoSuchMethodException e) {
            throw new EbatisException(e);
        }
        return future;
    }


    @Override
    public CreateIndexResponse create(String index, Map settings, String type, Map source) {
        return createAsync(index, settings, type, source).join();
    }

    @Override
    public CompletableFuture<CreateIndexResponse> createAsync(String index, Map settings, String type, Map source) {
        CompletableFuture<CreateIndexResponse> future = new CompletableFuture<>();
        Context context = ContextHolder.getContext();
        final CreateIndexRequest createIndexRequest = Requests.createIndexRequest(index);
        createIndexRequest.settings(settings);
        createIndexRequest.mapping(type, source);
        try {
            final Method createAsync = this.getClass().getDeclaredMethod("createAsync", String.class, Map.class, String.class, Map.class);
            final MethodMeta methodMeta = getMethodMeta(new String[]{index}, createAsync);
            final Object[] args = {index, settings, type, source};
            final Cluster cluster = clusterRouter.get().route(methodMeta);
            INTERCEPTOR.preRequest(args, cluster, methodMeta);
            INTERCEPTOR.postRequest(new DefaultRequestInfo<>(createIndexRequest, args));
            INTERCEPTOR.preResponse(new DefaultPreResponseInfo<>(createIndexRequest, null));
            cluster.highLevelClient().indices().createAsync(createIndexRequest, RequestOptions.DEFAULT, ActionListener.wrap(response -> {
                ContextHolder.setContext(context);
                try {
                    future.complete(response);
                    INTERCEPTOR.postResponse(new DefaultPostResponseInfo<>(createIndexRequest, response));
                } finally {
                    ContextHolder.remove();
                }
            }, exception -> {
                future.completeExceptionally(exception);
                INTERCEPTOR.handleException(exception);
                ContextHolder.remove();
            }));
        } catch (ConcurrentException | NoSuchMethodException e) {
            throw new EbatisException(e);
        }
        return future;
    }

    @Override
    public AcknowledgedResponse delete(String... indices) {
        return deleteAsync(indices).join();
    }

    @Override
    public CompletableFuture<AcknowledgedResponse> deleteAsync(String... indices) {
        CompletableFuture<AcknowledgedResponse> future = new CompletableFuture<>();
        Context context = ContextHolder.getContext();
        final DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indices);
        try {
            final Method deleteAsync = this.getClass().getDeclaredMethod("deleteAsync", String[].class);
            final MethodMeta methodMeta = getMethodMeta(indices, deleteAsync);
            final Cluster cluster = clusterRouter.get().route(methodMeta);
            final Object[] args = {indices};
            INTERCEPTOR.preRequest(args, cluster, methodMeta);
            INTERCEPTOR.postRequest(new DefaultRequestInfo<>(deleteIndexRequest, args));
            INTERCEPTOR.preResponse(new DefaultPreResponseInfo<>(deleteIndexRequest, null));
            cluster.highLevelClient().indices().deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, ActionListener.wrap(response -> {
                ContextHolder.setContext(context);
                try {
                    future.complete(response);
                    INTERCEPTOR.postResponse(new DefaultPostResponseInfo<>(deleteIndexRequest, response));
                } finally {
                    ContextHolder.remove();
                }
            }, exception -> {
                future.completeExceptionally(exception);
                INTERCEPTOR.handleException(exception);
                ContextHolder.remove();
            }));
        } catch (ConcurrentException | NoSuchMethodException e) {
            throw new EbatisException(e);
        }
        return future;
    }

    private MethodMeta getMethodMeta(String[] indices, Method method) {
        return new MethodMeta() {
            @Override
            public Class<?> getReturnType() {
                return null;
            }

            @Override
            public Optional<Class<?>> unwrappedReturnType() {
                return Optional.empty();
            }

            @Override
            public String[] getIndices(MethodMeta meta, Object[] args) {
                return indices;
            }

            @Override
            public String[] getTypes(MethodMeta meta, Object[] args) {
                return new String[0];
            }

            @Override
            public RequestType getRequestType() {
                return null;
            }

            @Override
            public ResultType getResultType() {
                return null;
            }

            @Override
            public <A extends Annotation> A getRequestAnnotation() {
                return null;
            }

            @Override
            public HttpConfig getHttpConfig() {
                return null;
            }

            @Override
            public List<ParameterMeta> getParameterMetas() {
                return Collections.emptyList();
            }

            @Override
            public ParameterMeta getConditionParameter() {
                return null;
            }

            @Override
            public Optional<ParameterMeta> findConditionParameter() {
                return Optional.empty();
            }

            @Override
            public Optional<ParameterMeta> getPageableParameter() {
                return Optional.empty();
            }

            @Override
            public ParameterMeta getResponseExtractorParameter() {
                return null;
            }

            @Override
            public String[] getIncludeFields() {
                return new String[0];
            }

            @Override
            public ResponseExtractor<?> getResponseExtractor(Object[] args) {
                return null;
            }

            @Override
            public Method getElement() {
                return method;
            }
        };
    }
}

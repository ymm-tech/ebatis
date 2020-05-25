package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.annotation.Bulk;
import com.ymm.ebatis.annotation.Delete;
import com.ymm.ebatis.annotation.DeleteByQuery;
import com.ymm.ebatis.annotation.Index;
import com.ymm.ebatis.annotation.MultiSearch;
import com.ymm.ebatis.annotation.Search;
import com.ymm.ebatis.annotation.Update;
import com.ymm.ebatis.annotation.UpdateByQuery;
import com.ymm.ebatis.annotation.MultiMatch;
import com.ymm.ebatis.executor.AggMethodExecutor;
import com.ymm.ebatis.core.executor.BulkMethodExecutor;
import com.ymm.ebatis.core.executor.DeleteByQueryMethodExecutor;
import com.ymm.ebatis.core.executor.DeleteMethodExecutor;
import com.ymm.ebatis.core.executor.IndexMethodExecutor;
import com.ymm.ebatis.executor.MethodExecutor;
import com.ymm.ebatis.executor.MultiSearchMethodExecutor;
import com.ymm.ebatis.core.executor.SearchMethodExecutor;
import com.ymm.ebatis.executor.UpdateByQueryMethodExecutor;
import com.ymm.ebatis.core.executor.UpdateMethodExecutor;
import com.ymm.ebatis.request.BulkRequestFactory;
import com.ymm.ebatis.request.DeleteByQueryRequestFactory;
import com.ymm.ebatis.request.DeleteRequestFactory;
import com.ymm.ebatis.request.IndexRequestFactory;
import com.ymm.ebatis.request.MultiSearchRequestFactory;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.request.SearchRequestFactory;
import com.ymm.ebatis.request.UpdateByQueryRequestFactory;
import com.ymm.ebatis.request.UpdateRequestFactory;
import com.ymm.ebatis.executor.DeleteByQueryMethodExecutor;
import com.ymm.ebatis.executor.DeleteMethodExecutor;
import com.ymm.ebatis.executor.IndexMethodExecutor;
import com.ymm.ebatis.executor.SearchMethodExecutor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求类型
 *
 * @author 章多亮
 * @since 2019/12/19 14:44
 */
@Getter
public enum RequestType {
    /**
     * 索引请求
     */
    INDEX(Index.class, IndexRequestFactory.INSTANCE, IndexMethodExecutor.INSTANCE),
    /**
     * 批量请求
     */
    BULK(Bulk.class, BulkRequestFactory.INSTANCE, BulkMethodExecutor.INSTANCE),
    /**
     * 删除请求
     */
    DELETE(Delete.class, DeleteRequestFactory.INSTANCE, DeleteMethodExecutor.INSTANCE),
    /**
     * 查询删除
     */
    DELETE_BY_QUERY(DeleteByQuery.class, DeleteByQueryRequestFactory.INSTANCE, DeleteByQueryMethodExecutor.INSTANCE),
    /**
     * 更新请求
     */
    UPDATE(Update.class, UpdateRequestFactory.INSTANCE, UpdateMethodExecutor.INSTANCE),
    /**
     * 查询更新
     */
    UPDATE_BY_QUERY(UpdateByQuery.class, UpdateByQueryRequestFactory.INSTANCE, UpdateByQueryMethodExecutor.INSTANCE),
    /**
     * 查询请求
     */
    SEARCH(Search.class, SearchRequestFactory.INSTANCE, SearchMethodExecutor.INSTANCE),
    MULTI_SEARCH(MultiSearch.class, MultiSearchRequestFactory.INSTANCE, MultiSearchMethodExecutor.INSTANCE),
    MULTI_MATCH(MultiMatch.class, MultiSearchRequestFactory.INSTANCE, MultiSearchMethodExecutor.INSTANCE),
    /**
     * 聚合查询
     */
    AGG(Agg.class, SearchRequestFactory.INSTANCE, AggMethodExecutor.INSTANCE);
    private static final Map<Class<? extends Annotation>, RequestType> ANNOTATION_EXECUTOR_TYPES;

    static {
        ANNOTATION_EXECUTOR_TYPES = Stream.of(values()).collect(Collectors.toMap(RequestType::getAnnotationClass, t -> t));
    }

    private final Class<? extends Annotation> annotationClass;
    private final MethodExecutor executor;
    private final RequestFactory factory;

    RequestType(Class<? extends Annotation> annotationClass, RequestFactory factory, MethodExecutor executor) {
        this.annotationClass = annotationClass;
        this.factory = factory;
        this.executor = executor;
    }

    /**
     * 根据指定方法，获取方法的执行器；先通过注解获取，注解没有匹配的类型，换方法前缀匹配
     *
     * @param method Mapper方法
     * @return 方法执行类型
     */
    public static Optional<RequestType> valueOf(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            RequestType type = ANNOTATION_EXECUTOR_TYPES.get(annotation.annotationType());
            if (type != null) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }


    public static Optional<RequestType> valueOf(Class<?> annotationClass) {
        return Optional.ofNullable(ANNOTATION_EXECUTOR_TYPES.get(annotationClass));
    }
}

package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.annotation.Bulk;
import com.ymm.ebatis.core.annotation.Delete;
import com.ymm.ebatis.core.annotation.DeleteByQuery;
import com.ymm.ebatis.core.annotation.Index;
import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.annotation.Update;
import com.ymm.ebatis.core.annotation.UpdateByQuery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2019/12/17 15:50
 */
public enum RequestFactoryType {
    /**
     * 索引构建器
     */
    INDEX(Index.class, IndexRequestFactory.INSTANCE),
    /**
     * 批量索引
     */
    BULK(Bulk.class, BulkRequestFactory.INSTANCE),
    /**
     * 删除构建器
     */
    DELETE(Delete.class, DeleteRequestFactory.INSTANCE),
    /**
     * 查询删除
     */
    DELETE_BY_QUERY(DeleteByQuery.class, DeleteByQueryRequestFactory.INSTANCE),
    /**
     * 更新构建器
     */
    UPDATE(Update.class, UpdateRequestFactory.INSTANCE),
    /**
     * 查询更新
     */
    UPDATE_BY_QUERY(UpdateByQuery.class, UpdateByQueryRequestFactory.INSTANCE),
    /**
     * 查询构建器
     */
    QUERY(Search.class, SearchRequestFactory.INSTANCE),
    MULTI_SEARCH(MultiSearch.class, MultiSearchRequestFactory.INSTANCE),

    AGG(Agg.class, AggRequestFactory.INSTANCE);

    /**
     * 方法注解注解和构建器映射
     */
    private static final Map<Class<? extends Annotation>, RequestFactoryType> REQUEST_FACTORIES;

    static {
        REQUEST_FACTORIES = Stream.of(RequestFactoryType.values()).collect(Collectors.toMap(RequestFactoryType::getAnnotationClass, t -> t));
    }

    /**
     * 构建器指示注解类型
     */
    private final Class<? extends Annotation> annotationClass;
    /**
     * 构建器
     */
    private final RequestFactory requestFactory;

    RequestFactoryType(Class<? extends Annotation> annotationClass, RequestFactory requestFactory) {
        this.annotationClass = annotationClass;
        this.requestFactory = requestFactory;
    }

    /**
     * 根据方法的获取对应的语句构建器类型
     *
     * @param method 方法注
     * @return 构建器
     */
    public static Optional<RequestFactoryType> from(Method method) {
        for (RequestFactoryType type : values()) {
            if (method.isAnnotationPresent(type.getAnnotationClass())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据方法的注解类型获取对应的语句构建器
     *
     * @param annotationClass 方法注解类型
     * @return 构建器
     */
    public static Optional<RequestFactoryType> valueOf(Class<? extends Annotation> annotationClass) {
        return Optional.ofNullable(REQUEST_FACTORIES.get(annotationClass));
    }

    /**
     * 获取构建类型枚举支持的注解
     *
     * @return 方法注解
     */
    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    /**
     * 后去构建类型枚举对应的构建器
     *
     * @return 构建器
     */
    public RequestFactory getRequestFactory() {
        return requestFactory;
    }
}

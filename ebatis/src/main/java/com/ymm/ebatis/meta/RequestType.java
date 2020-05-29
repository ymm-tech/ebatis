package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.annotation.Bulk;
import com.ymm.ebatis.annotation.Delete;
import com.ymm.ebatis.annotation.DeleteByQuery;
import com.ymm.ebatis.annotation.Index;
import com.ymm.ebatis.annotation.MultiMatch;
import com.ymm.ebatis.annotation.MultiSearch;
import com.ymm.ebatis.annotation.Search;
import com.ymm.ebatis.annotation.Update;
import com.ymm.ebatis.annotation.UpdateByQuery;
import com.ymm.ebatis.executor.RequestExecutor;
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
    INDEX(Index.class, RequestExecutor.index()),
    /**
     * 批量请求
     */
    BULK(Bulk.class, RequestExecutor.bulk()),
    /**
     * 删除请求
     */
    DELETE(Delete.class, RequestExecutor.delete()),
    /**
     * 查询删除
     */
    DELETE_BY_QUERY(DeleteByQuery.class, RequestExecutor.deleteByQuery()),
    /**
     * 更新请求
     */
    UPDATE(Update.class, RequestExecutor.update()),
    /**
     * 查询更新
     */
    UPDATE_BY_QUERY(UpdateByQuery.class, RequestExecutor.updateByQuery()),
    /**
     * 查询请求
     */
    SEARCH(Search.class, RequestExecutor.search()),
    MULTI_SEARCH(MultiSearch.class, RequestExecutor.multiSearch()),
    MULTI_MATCH(MultiMatch.class, RequestExecutor.multiSearch()),
    /**
     * 聚合查询
     */
    AGG(Agg.class, RequestExecutor.agg());
    private static final Map<Class<? extends Annotation>, RequestType> ANNOTATION_EXECUTOR_TYPES;

    static {
        ANNOTATION_EXECUTOR_TYPES = Stream.of(values()).collect(Collectors.toMap(RequestType::getAnnotationClass, t -> t));
    }

    private final Class<? extends Annotation> annotationClass;
    private final RequestExecutor requestExecutor;

    RequestType(Class<? extends Annotation> annotationClass, RequestExecutor requestExecutor) {
        this.annotationClass = annotationClass;
        this.requestExecutor = requestExecutor;
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

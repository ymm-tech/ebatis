package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Agg;
import com.ymm.ebatis.core.annotation.Bulk;
import com.ymm.ebatis.core.annotation.Cat;
import com.ymm.ebatis.core.annotation.Delete;
import com.ymm.ebatis.core.annotation.DeleteByQuery;
import com.ymm.ebatis.core.annotation.Get;
import com.ymm.ebatis.core.annotation.Index;
import com.ymm.ebatis.core.annotation.MultiSearch;
import com.ymm.ebatis.core.annotation.Search;
import com.ymm.ebatis.core.annotation.SearchScroll;
import com.ymm.ebatis.core.annotation.Update;
import com.ymm.ebatis.core.annotation.UpdateByQuery;
import com.ymm.ebatis.core.domain.ScrollResponse;
import com.ymm.ebatis.core.executor.RequestExecutor;
import com.ymm.ebatis.core.generic.GenericType;
import com.ymm.ebatis.core.provider.ScrollProvider;
import lombok.Getter;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
    SEARCH(Search.class, RequestExecutor.search()) {
        @Override
        public Optional<Class<?>> getEntityClass(MethodMeta meta) {
            GenericType genericType = getReturnGenericType(meta);
            if (genericType.is(SearchResponse.class)) {
                return Optional.empty();
            }

            return genericType.resolveGenericOptional(0);
        }
    },
    /**
     * search scroll request
     */
    SEARCH_SCROLL(SearchScroll.class, RequestExecutor.searchScroll()) {
        @Override
        public boolean validate(MethodMeta meta) {
            SearchScroll scroll = meta.getAnnotation(SearchScroll.class);
            if (scroll.clearScroll()) {
                return true;
            }

            return meta.getConditionParameter().isAssignableTo(ScrollProvider.class);
        }

        @Override
        public Optional<Class<?>> getEntityClass(MethodMeta meta) {
            GenericType genericType = RequestType.getReturnGenericType(meta);

            if (genericType.is(SearchResponse.class)) {
                return Optional.empty();
            }

            if (genericType.is(ScrollResponse.class)) {
                return genericType.resolveGenericOptional(0);
            }

            return super.getEntityClass(meta);
        }
    },
    MULTI_SEARCH(MultiSearch.class, RequestExecutor.multiSearch()) {
        @Override
        public Optional<Class<?>> getEntityClass(MethodMeta meta) {
            GenericType genericType = getReturnGenericType(meta);
            if (genericType.is(MultiSearchResponse.class)) {
                return super.getEntityClass(meta);
            }

            return genericType.resolveGenericOptional(0, 0);
        }
    },
    /**
     * 聚合查询
     */
    AGG(Agg.class, RequestExecutor.agg()),
    GET(Get.class, RequestExecutor.get()) {
        @Override
        public Optional<Class<?>> getEntityClass(MethodMeta meta) {
            GenericType genericType = RequestType.getReturnGenericType(meta);
            if (genericType.is(GetResponse.class)) {
                return super.getEntityClass(meta);
            }

            return Optional.ofNullable(genericType.resolve());
        }
    },
    CAT(Cat.class, RequestExecutor.cat()),
    ;

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

    private static GenericType getReturnGenericType(MethodMeta meta) {
        Method method = meta.getElement();
        GenericType genericType = GenericType.forMethod(method).returnType();

        Class<?> returnType;
        while (CompletableFuture.class == (returnType = genericType.resolve()) || Optional.class == returnType) {
            genericType = genericType.resolveType(0);
        }
        return genericType;
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

    public boolean validate(MethodMeta meta) {
        return meta != null;
    }

    public Optional<Class<?>> getEntityClass(MethodMeta meta) {
        return Optional.empty();
    }
}

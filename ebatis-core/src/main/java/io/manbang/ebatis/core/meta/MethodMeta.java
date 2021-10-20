package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.domain.HttpConfig;
import io.manbang.ebatis.core.domain.Pageable;
import io.manbang.ebatis.core.response.ResponseExtractor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/27 17:34
 */
public interface MethodMeta extends AnnotatedMeta<Method> {
    static MethodMeta of(MapperInterface mapperInterface, Method method) {
        return new DefaultMapperMethodMeta(mapperInterface, method);
    }

    Class<?> getReturnType();

    Optional<Class<?>> unwrappedReturnType();

    default void setPageable(Object[] args) {
        getPageableParameter()
                .map(p -> (Pageable) p.getValue(args))
                .ifPresent(ContextHolder::setPageable);
    }

    /**
     * 获取接口要操作的所有索引
     *
     * @return 索引列表
     */
    String[] getIndices(MethodMeta meta, Object[] args);

    String[] getTypes(MethodMeta meta, Object[] args);

    RequestType getRequestType();

    ResultType getResultType();

    <A extends Annotation> A getRequestAnnotation();

    HttpConfig getHttpConfig();

    List<ParameterMeta> getParameterMetas();

    ParameterMeta getConditionParameter();

    Optional<ParameterMeta> findConditionParameter();

    Optional<ParameterMeta> getPageableParameter();

    ParameterMeta getResponseExtractorParameter();

    default String getIndex(MethodMeta meta, Object[] args) {
        return getIndices(meta, args)[0];
    }

    default String getType(MethodMeta meta, Object[] args) {
        if (ArrayUtils.isEmpty(getTypes(meta, args))) {
            return StringUtils.EMPTY;
        }
        return getTypes(meta, args)[0];
    }

    String[] getIncludeFields();

    ResponseExtractor<?> getResponseExtractor(Object[] args);
}

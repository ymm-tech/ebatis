package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionRequest;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

/**
 * @author 章多亮
 * @since 2019/12/17 15:32
 */
public abstract class AbstractRequestFactory<A extends Annotation, R extends ActionRequest> implements RequestFactory<R> {

    @Override
    public final R create(MethodMeta meta, Object... args) {
        R request = doCreate(meta, args);
        setAnnotationMeta(request, meta.getRequestAnnotation());
        return request;
    }

    protected void setTypeIfNecessary(MethodMeta meta, Consumer<String> consumer) {
        if (StringUtils.isNotBlank(meta.getType())) {
            consumer.accept(meta.getType());
        }
    }

    protected void setTypesIfNecessary(MethodMeta meta, Consumer<String[]> consumer) {
        if (ArrayUtils.isNotEmpty(meta.getTypes())) {
            consumer.accept(meta.getTypes());
        }
    }


    /**
     * 设置可选信息，通过注解传递过来
     *
     * @param request    请求
     * @param annotation 注解
     */
    protected abstract void setAnnotationMeta(R request, A annotation);

    /**
     * 实际构建查询请求
     *
     * @param meta 方法元数据
     * @param args 查询条件
     * @return 请求实例
     */
    protected abstract R doCreate(MethodMeta meta, Object[] args);
}

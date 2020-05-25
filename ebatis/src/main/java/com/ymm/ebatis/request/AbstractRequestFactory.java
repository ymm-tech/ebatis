package com.ymm.ebatis.request;

import com.ymm.ebatis.meta.BeanDescriptor;
import com.ymm.ebatis.meta.MethodMeta;
import org.elasticsearch.action.ActionRequest;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

/**
 * @author 章多亮
 * @since 2019/12/17 15:32
 */
public abstract class AbstractRequestFactory<A extends Annotation, R extends ActionRequest> implements RequestFactory {
    private final Class<A> annotationClass;

    protected AbstractRequestFactory() {
        this.annotationClass = (Class<A>) ResolvableType.forClass(this.getClass()).as(AbstractRequestFactory.class).resolveGeneric(0);
    }

    @Override
    public final <T extends ActionRequest> T create(MethodMeta meta, Object... args) {
        R request = doCreate(meta, args);
        meta.getAnnotation(annotationClass).ifPresent(a -> setOptionalMeta(request, a));
        return (T) request;
    }

    /**
     * 设置可选信息，通过注解传递过来
     *
     * @param request    请求
     * @param annotation 注解
     */
    protected abstract void setOptionalMeta(R request, A annotation);

    /**
     * 实际构建查询请求
     *
     * @param meta 方法元数据
     * @param args 查询条件
     * @return 请求实例
     */
    protected abstract R doCreate(MethodMeta meta, Object... args);

    /**
     * 设置Id或Version
     *
     * @param doc             文档
     * @param idFunction      id设置方法
     * @param versionFunction 版本设置方法
     */
    protected final void setIdAndVersion(Object doc, Consumer<String> idFunction, Consumer<Long> versionFunction) {
        BeanDescriptor descriptor = BeanDescriptor.of(doc);

        descriptor.getId().map(e -> e.getValue(doc)).map(String::valueOf).ifPresent(idFunction);
        descriptor.getVersion().map(e -> e.getValue(doc)).map(long.class::cast).ifPresent(versionFunction);
    }
}

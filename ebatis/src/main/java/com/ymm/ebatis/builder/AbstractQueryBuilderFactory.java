package com.ymm.ebatis.builder;

import com.ymm.ebatis.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.util.Optional;

abstract class AbstractQueryBuilderFactory<B extends QueryBuilder, A extends Annotation> implements QueryBuilderFactory {
    private final Class<A> annotationClass;

    @SuppressWarnings("unchecked")
    protected AbstractQueryBuilderFactory() {
        this.annotationClass = (Class<A>) ResolvableType.forClass(this.getClass()).as(AbstractQueryBuilderFactory.class).resolveGeneric(1);
    }

    @Override
    public final QueryBuilder create(ConditionMeta<?> conditionMeta, Object condition) {
        B builder = doCreate(conditionMeta, condition);
        Optional.ofNullable(conditionMeta).flatMap(c -> c.getAttributeAnnotation(annotationClass)).ifPresent(annotation -> setOptionalMeta(builder, annotation));
        return builder;
    }

    protected void setOptionalMeta(B builder, A annotation) {
        // do nothing
    }

    /**
     * 创建查询构建器
     *
     * @param conditionMeta 查询条件实例
     * @param condition     条件
     * @return 查询构建器
     */
    protected abstract B doCreate(ConditionMeta<?> conditionMeta, Object condition);
}

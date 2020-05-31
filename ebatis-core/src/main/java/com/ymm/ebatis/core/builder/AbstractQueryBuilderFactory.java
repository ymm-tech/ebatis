package com.ymm.ebatis.core.builder;

import com.ymm.ebatis.core.meta.ConditionMeta;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;

/**
 * QueryBuilder抽象工厂
 *
 * @param <B> 待创建的QueryBuilder类型
 * @param <A> 类型对应的注解
 * @see com.ymm.ebatis.core.annotation.QueryType
 */
abstract class AbstractQueryBuilderFactory<B extends QueryBuilder, A extends Annotation> implements QueryBuilderFactory {
    private final Class<A> attributeAnnotationClass;

    @SuppressWarnings("unchecked")
    protected AbstractQueryBuilderFactory() {
        attributeAnnotationClass = (Class<A>) ResolvableType.forClass(getClass())
                .as(AbstractQueryBuilderFactory.class).resolveGeneric(1);
    }

    @Override
    public QueryBuilder create(ConditionMeta meta, Object condition) {
        // 如果条件为空是否需要处理，比如exists查询，它其实是不要传入实际条件的
        if (condition == null && onlyHandleNoneNullable()) {
            return null;
        }

        B builder = doCreate(meta, condition);

        if (meta != null) {
            meta.findAttributeAnnotation(attributeAnnotationClass).ifPresent(attr -> setAnnotationMeta(builder, attr));
        }

        return builder;
    }

    /**
     * 判断是否只能处理非空条件，默认为 <code>true</code>，表示不能处理空值条件
     *
     * @return 如果条件不能为空，返回<code>true</code>
     */
    protected boolean onlyHandleNoneNullable() {
        return true;
    }

    protected void setAnnotationMeta(B builder, A annotation) {
        // do nothing
    }

    /**
     * 创建查询构建器
     *
     * @param meta      查询条件实例
     * @param condition 条件
     * @return 查询构建器
     */
    protected abstract B doCreate(ConditionMeta meta, Object condition);
}

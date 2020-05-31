package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.builder.QueryBuilderFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author 章多亮
 * @since 2020/5/27 17:44
 */
public interface FieldMeta extends AnnotatedMeta<Field>, ConditionMeta {

    static FieldMeta of(Field field) {
        return new DefaultFieldMeta(field);
    }

    @Override
    String getName();

    Object getValue(Object instance);

    Class<? extends Annotation> getQueryClauseAnnotationClass();

    QueryBuilderFactory getQueryBuilderFactory();
}

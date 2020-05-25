package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Search;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Optional;

/**
 * @author 章多亮
 */
public class ParameterConditionMeta extends AbstractConditionMeta<Parameter> {
    public ParameterConditionMeta(Parameter element) {
        super(element);
    }

    @Override
    protected String getNameFromElement(Parameter element) {
        return element.getName();
    }

    @Override
    protected Class<?> getConditionType(Parameter element) {
        Class<?> parameterType = element.getType();
        if (Collection.class.isAssignableFrom(parameterType)) {
            return ResolvableType.forType(element.getParameterizedType()).resolveGeneric(0);
        } else if (parameterType.isArray()) {
            return parameterType.getComponentType();
        } else {
            return element.getType();
        }
    }

    @Override
    protected Annotation getQueryClauseAnnotation(AnnotatedElement element) {
        return getParentAnnotation(Search.class).orElse(null);
    }

    @Override
    public <A extends Annotation> Optional<A> getParentAnnotation(Class<A> aClass) {
        return Optional.ofNullable(getElement().getDeclaringExecutable().getAnnotation(aClass));
    }

    @Override
    public <T> T getValue(Object instance) {
        return (T) instance;
    }

    @Override
    protected boolean getArrayOrCollectionBasicCondition(Parameter element) {
        return false;
    }
}

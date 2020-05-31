package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.common.AnnotationUtils;
import com.ymm.ebatis.core.domain.Pageable;
import com.ymm.ebatis.core.response.ResponseExtractor;
import lombok.ToString;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/5/27 19:05
 */
@ToString(of = "parameter")
public class DefaultParameterMeta extends AbstractConditionMeta implements ParameterMeta {
    private final Parameter parameter;
    private final int index;
    private final boolean pageable;
    private final boolean responseExtractor;
    private final boolean basic;
    private final boolean basicArrayOrCollection;
    private final String name;
    private final Annotation requestAnnotation;

    public DefaultParameterMeta(MethodMeta methodMeta, Parameter parameter, int index) {
        super(parameter.getType());
        this.parameter = parameter;
        this.index = index;
        this.name = parameter.getName();

        Class<?> type = getActualType(parameter);
        this.basic = MetaUtils.isBasic(type);
        this.pageable = Pageable.class == type;
        this.responseExtractor = ResponseExtractor.class == type;
        this.basicArrayOrCollection = isArrayOrCollection() || basic;
        this.requestAnnotation = methodMeta.getRequestAnnotation();
    }

    private Class<?> getActualType(Parameter parameter) {
        Class<?> type;
        if (isArray()) {
            type = getType().getComponentType();
        } else if (isCollection()) {
            type = ResolvableType.forMethodParameter((Method) parameter.getDeclaringExecutable(), index).resolveGeneric(0);
        } else {
            type = getType();
        }
        return type;
    }

    @Override
    public Parameter getElement() {
        return parameter;
    }

    @Override
    public Map<Class<? extends Annotation>, List<FieldMeta>> getQueryClauses(Object instance) {
        return ClassMeta.parameter(parameter, instance == null ? null : instance.getClass()).getQueryClauses();
    }

    @Override
    public boolean isPageable() {
        return pageable;
    }

    @Override
    public boolean isResponseExtractor() {
        return responseExtractor;
    }

    @Override
    public int getIndex() {
        return index;
    }


    @Override
    public boolean isBasic() {
        return basic;
    }

    @Override
    public boolean isBasicArrayOrCollection() {
        return basicArrayOrCollection;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <A extends Annotation> Optional<A> findAttributeAnnotation(Class<A> annotationClass) {
        return AnnotationUtils.findAttribute(requestAnnotation, annotationClass);
    }

    @Override
    public Object getValue(Object[] args) {
        return args[index];
    }
}

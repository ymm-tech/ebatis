package io.manbang.ebatis.core.generic;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author 章多亮
 * @since 2020/6/9 11:32
 */
public class DefaultMethodGenericType extends DefaultGenericType implements MethodGenericType {
    private final Method method;

    public DefaultMethodGenericType(Method method) {
        super(method.getReturnType());
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public GenericType returnType() {
        Type genericReturnType = method.getGenericReturnType();
        return GenericType.forType(genericReturnType);
    }

    @Override
    public GenericType parameterType(int index) {
        return GenericType.forType(method.getGenericParameterTypes()[index]);
    }
}

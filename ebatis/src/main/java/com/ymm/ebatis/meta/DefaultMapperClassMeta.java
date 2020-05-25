package com.ymm.ebatis.meta;

import com.ymm.ebatis.annotation.Mapper;
import com.ymm.ebatis.exception.MapperClassIsNotInterfaceException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/5/22 15:34
 */
class DefaultMapperClassMeta implements MapperClassMeta {
    private final Class<?> clazz;
    private final Mapper mapper;
    private final String[] indices;
    private final String[] types;
    private final String[] routing;
    private final Map<Method, MethodMeta> methods;

    DefaultMapperClassMeta(Class<?> clazz) {
        this.clazz = clazz;
        mapperClassMustBeInterface(clazz);

        this.mapper = getAnnotationRequired(Mapper.class);
        this.indices = mapper.indices();
        this.types = mapper.types();
        this.routing = mapper.routing();
        this.methods = getMethods(clazz);
    }

    private void mapperClassMustBeInterface(Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new MapperClassIsNotInterfaceException(clazz.toString());
        }
    }

    private Map<Method, MethodMeta> getMethods(Class<?> clazz) {
        Map<Method, MethodMeta> metas = Stream.of(clazz.getMethods())
                // 非静态方法
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                // 非Object声明的方法
                .filter(method -> method.getDeclaringClass() != Object.class)
                .collect(Collectors.toMap(method -> method, MethodMeta::from));

        return Collections.unmodifiableMap(metas);
    }

    @Override
    public String[] routing() {
        return routing;
    }

    @Override
    public String[] indices() {
        return indices;
    }

    @Override
    public String[] types() {
        return types;
    }

    @Override
    public MethodMeta getMethodMeta(Method method) {
        return methods.get(method);
    }

    @Override
    public Mapper getMapper() {
        return mapper;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }
}

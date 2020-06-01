package com.ymm.ebatis.core.meta;

import com.ymm.ebatis.core.annotation.Http;
import com.ymm.ebatis.core.annotation.Mapper;
import com.ymm.ebatis.core.domain.HttpConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 章多亮
 * @since 2020/5/27 18:30
 */
class DefaultMapperInterface implements MapperInterface {
    private final Class<?> mapperInterface;
    private final Mapper mapper;
    private final Map<Method, MapperMethod> mapperMethods;
    private final HttpConfig httpConfig;


    DefaultMapperInterface(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
        this.httpConfig = getHttpConfig(mapperInterface);
        this.mapper = getAnnotation(Mapper.class);

        this.mapperMethods = getMapperMethods(mapperInterface);
    }

    private HttpConfig getHttpConfig(Class<?> mapperInterface) {
        Http http = mapperInterface.getAnnotation(Http.class);

        return http == null ? null : new HttpConfig()
                .socketTimeout(http.socketTimeout())
                .connectTimeout(http.connectTimeout())
                .connectionRequestTimeout(http.connectionRequestTimeout());
    }

    private Map<Method, MapperMethod> getMapperMethods(Class<?> mapperInterface) {
        Map<Method, MapperMethod> methods = Arrays.stream(mapperInterface.getDeclaredMethods())
                .filter(this::filterMethod)
                .map(m -> MapperMethod.of(this, m))
                .collect(Collectors.toMap(MethodMeta::getElement, m -> m));

        return Collections.unmodifiableMap(methods);
    }

    private boolean filterMethod(Method method) {
        // 非静态方法
        return !Modifier.isStatic(method.getModifiers());
    }

    @Override
    public String[] getIndices() {
        return mapper.indices();
    }

    @Override
    public String[] getTypes() {
        return mapper.types();
    }

    @Override
    public String getRouting() {
        return mapper.routing();
    }

    @Override
    public String getClusterRouter() {
        return mapper.clusterRouter();
    }

    @Override
    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    @Override
    public MapperMethod getMapperMethod(Method method) {
        return mapperMethods.get(method);
    }

    @Override
    public Class<?> getElement() {
        return mapperInterface;
    }
}

package io.manbang.ebatis.core.meta;

import io.manbang.ebatis.core.annotation.Http;
import io.manbang.ebatis.core.annotation.Mapper;
import io.manbang.ebatis.core.common.AnnotationUtils;
import io.manbang.ebatis.core.domain.HttpConfig;
import io.manbang.ebatis.core.exception.AttributeNotFoundException;
import io.manbang.ebatis.core.exception.InstanceException;
import io.manbang.ebatis.core.exception.MapperAnnotationNotPresentException;
import io.manbang.ebatis.core.mapper.MappingRouter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 章多亮
 * @since 2020/5/27 18:30
 */
class DefaultMapperInterface implements MapperInterface {
    private final Class<?> mapperType;
    private final String[] indices;
    private final String[] types;
    private final String clusterRouter;
    private final MappingRouter mappingRouter;
    private final Map<Method, MapperMethod> mapperMethods;
    private final HttpConfig httpConfig;


    DefaultMapperInterface(Class<?> mapperType) {
        this.mapperType = mapperType;
        this.httpConfig = getHttpConfig(mapperType);

        Annotation mapperAnnotation = getMapperAnnotation(mapperType);

        this.indices = getIndices(mapperAnnotation);
        this.types = getTypes(mapperAnnotation);
        this.clusterRouter = getClusterRouter(mapperAnnotation);
        this.mappingRouter = getMappingRouter(mapperAnnotation);

        this.mapperMethods = getMapperMethods(mapperType);
    }

    private String getClusterRouter(Annotation mapperAnnotation) {
        return getAnnotationAttribute(mapperAnnotation, "clusterRouter", false);
    }

    private String[] getTypes(Annotation mapperAnnotation) {
        return getAnnotationAttribute(mapperAnnotation, "types", false);
    }

    private String[] getIndices(Annotation mapperAnnotation) {
        return getAnnotationAttribute(mapperAnnotation, "indices", true);
    }

    private MappingRouter getMappingRouter(Annotation mapperAnnotation) {
        Class<? extends MappingRouter> mappingRouterClazz = getAnnotationAttribute(mapperAnnotation, "mappingRouter", true);
        try {
            if (MappingRouter.class.equals(mappingRouterClazz)) {
                return null;
            }
            return mappingRouterClazz.newInstance();
        } catch (Exception e) {
            throw new InstanceException(e);
        }
    }

    private <A> A getAnnotationAttribute(Annotation mapperAnnotation, String attributeName, boolean required) {
        Optional<A> attribute = AnnotationUtils.findAttribute(mapperAnnotation, attributeName);
        if (attribute.isPresent()) {
            return attribute.get();
        }

        if (required) {
            throw new AttributeNotFoundException(mapperAnnotation.annotationType().getName() + '#' + attributeName);
        }
        return null;
    }

    private Annotation getMapperAnnotation(Class<?> mapperInterface) {
        Annotation[] annotations = mapperInterface.getAnnotations();

        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            if (annotationType == Mapper.class || annotationType.isAnnotationPresent(Mapper.class)) {
                return annotation;
            }
        }

        throw new MapperAnnotationNotPresentException(mapperInterface.getName());
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
        return indices;
    }

    @Override
    public String[] getTypes() {
        return types;
    }

    @Override
    public MappingRouter getMappingRouter() {
        return mappingRouter;
    }

    @Override
    public String getClusterRouterName() {
        return clusterRouter;
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
        return mapperType;
    }
}

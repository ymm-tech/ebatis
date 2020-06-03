package com.ymm.ebatis.core.meta;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 章多亮
 * @since 2020/6/3 14:11
 */
class CachedAnnotationMeta implements AnnotationMeta {
    private static final Map<Class<? extends Annotation>, AnnotationMeta> METAS = new HashMap<>();

    private final Class<? extends Annotation> annotationType;
    private final List<AnnotationAttribute> attributes;

    private CachedAnnotationMeta(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
        this.attributes = getAttributes(annotationType);
    }

    static AnnotationMeta of(Class<? extends Annotation> annotationClass) {
        return METAS.computeIfAbsent(annotationClass, CachedAnnotationMeta::new);
    }

    private List<AnnotationAttribute> getAttributes(Class<? extends Annotation> annotationClass) {
        return Collections.unmodifiableList(Stream.of(annotationClass.getDeclaredMethods())
                .map(AnnotationAttribute::of)
                .collect(Collectors.toList()));
    }

    @Override
    public List<AnnotationAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    @Override
    public Class<? extends Annotation> getElement() {
        return getAnnotationType();
    }
}

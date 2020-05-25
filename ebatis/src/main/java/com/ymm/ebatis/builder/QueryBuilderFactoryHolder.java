package com.ymm.ebatis.builder;

import java.lang.annotation.Annotation;
import java.util.Optional;

class QueryBuilderFactoryHolder {
    private final Annotation annotation;
    private final QueryBuilderFactory factory;

    QueryBuilderFactoryHolder(Annotation annotation, QueryBuilderFactory factory) {
        this.annotation = annotation;
        this.factory = factory;
    }

    Optional<QueryBuilderFactory> factory() {
        return Optional.ofNullable(factory);
    }

    Annotation annotation() {
        return annotation;
    }
}

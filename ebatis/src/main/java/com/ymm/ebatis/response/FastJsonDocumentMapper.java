package com.ymm.ebatis.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymm.ebatis.domain.AdditionalSource;
import com.ymm.ebatis.domain.MetaSource;
import com.ymm.ebatis.domain.ResponseMeta;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author duoliang.zhang
 */
public class FastJsonDocumentMapper<T> implements DocumentMapper<T> {
    private static final Map<Class<?>, DocumentMapper<?>> MAPPERS = new ConcurrentHashMap<>();
    private final Class<T> entityClass;

    private FastJsonDocumentMapper(Class<T> entityClass) { // NOSONAR
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    public static <T> DocumentMapper<T> of(Class<T> entityClass) {
        return (DocumentMapper<T>) MAPPERS.computeIfAbsent(entityClass, FastJsonDocumentMapper::new);
    }

    @Override
    public T mapRow(SearchHit hit, int index) {
        T document = null;
        try {
            document = new ObjectMapper().readValue(hit.getSourceRef().toBytesRef().bytes, entityClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document instanceof MetaSource) {
            MetaSource source = (MetaSource) document;
            ResponseMeta meta = MetaSource.of(hit);
            source.setResponseMeta(meta);
        }
        if (document instanceof AdditionalSource) {
            Map<String, List<Object>> additionalSourceMap = hit.getFields().values().stream()
                    .map(e -> Tuple.tuple(e.getName(), e.getValues()))
                    .collect(Collectors.toMap(Tuple::v1, Tuple::v2));
            AdditionalSource additionalSource = (AdditionalSource) document;
            additionalSource.setAdditionalSource(additionalSourceMap);
        }
        return document;
    }
}

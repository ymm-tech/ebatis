package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.common.ObjectMapperHolder;
import com.ymm.ebatis.core.domain.AdditionalSource;
import com.ymm.ebatis.core.domain.MetaSource;
import com.ymm.ebatis.core.domain.ResponseMeta;
import com.ymm.ebatis.core.exception.DocumentDeserializeException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JacksonDocumentMapper<T> implements DocumentMapper<T> {
    private static final Map<Class<?>, DocumentMapper<?>> MAPPERS = new ConcurrentHashMap<>();
    private final Class<T> entityClass;

    private JacksonDocumentMapper(Class<T> entityClass) { // NOSONAR
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    public static <T> DocumentMapper<T> of(Class<T> entityClass) {
        return (DocumentMapper<T>) MAPPERS.computeIfAbsent(entityClass, JacksonDocumentMapper::new);
    }

    @Override
    public T mapRow(SearchHit hit, int index) {
        T document;
        try {
            document = ObjectMapperHolder.objectMapper().readValue(hit.getSourceRef().toBytesRef().bytes, entityClass);
        } catch (IOException e) {
            log.error("反序列化文档异常", e);
            throw new DocumentDeserializeException(e);
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

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }
}

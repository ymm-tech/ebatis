package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.domain.AdditionalSource;
import io.manbang.ebatis.core.domain.HighlightSource;
import io.manbang.ebatis.core.domain.MetaSource;
import io.manbang.ebatis.core.domain.ResponseMeta;
import io.manbang.ebatis.core.exception.DocumentDeserializeException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Arrays;
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

        if (document instanceof HighlightSource) {
            final Map<String, List<String>> highlightSourceMap = hit.getHighlightFields().values().stream()
                    .map(h -> Tuple.tuple(h.getName(), Arrays.stream(h.fragments()).map(t -> t.string()).collect(Collectors.toList())))
                    .collect(Collectors.toMap(Tuple::v1, Tuple::v2));
            final HighlightSource highlightSource = (HighlightSource) document;
            highlightSource.setHighlightSource(highlightSourceMap);
        }
        return document;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }
}

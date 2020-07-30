package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.common.ObjectMapperHolder;
import io.manbang.ebatis.core.exception.DocumentDeserializeException;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author weilong.hu
 * @since 2020/7/2 14:24
 */
public class ArrayMultiGetResponseExtractor<T> implements MultiGetResponseExtractor<T[]> {
    private static final Map<Class<?>, ArrayMultiGetResponseExtractor<?>> MAPPERS = new ConcurrentHashMap<>();
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public static <T> ArrayMultiGetResponseExtractor<T> of(Class<T> entityClass) {
        return (ArrayMultiGetResponseExtractor<T>) MAPPERS.computeIfAbsent(entityClass, ArrayMultiGetResponseExtractor::new);
    }

    private ArrayMultiGetResponseExtractor(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] doExtractData(MultiGetResponse response) {
        MultiGetItemResponse[] responses = response.getResponses();
        T[] result = (T[]) Array.newInstance(entityClass, responses.length);
        for (int i = 0; i < responses.length; i++) {
            try {
                if (responses[i].getResponse().isExists()) {
                    result[i] = ObjectMapperHolder.objectMapper().readValue(responses[i].getResponse().getSourceAsBytes(), entityClass);
                }
            } catch (IOException e) {
                throw new DocumentDeserializeException(e);
            }
        }
        return result;
    }
}

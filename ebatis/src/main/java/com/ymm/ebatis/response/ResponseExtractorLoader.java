package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.MethodMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author 章多亮
 * @since 2019/12/20 10:35
 */
public class ResponseExtractorLoader {
    private static final Map<MethodMeta, ResponseExtractor<?>> RESPONSE_EXTRACTORS = new HashMap<>();

    private ResponseExtractorLoader() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public static <T> ResponseExtractor<T> getResponseExtractor(MethodMeta method) {
        return (ResponseExtractor<T>) RESPONSE_EXTRACTORS.computeIfAbsent(method, ResponseExtractorLoader::findResponseExtractor);
    }

    private static ResponseExtractor<?> findResponseExtractor(MethodMeta method) {
        ServiceLoader<ResponseExtractorProvider> providers = ServiceLoader.load(ResponseExtractorProvider.class);
        for (ResponseExtractorProvider provider : providers) {
            if (provider.support(method)) {
                return provider.getResponseExtractor(method);
            }
        }
        return null;
    }
}

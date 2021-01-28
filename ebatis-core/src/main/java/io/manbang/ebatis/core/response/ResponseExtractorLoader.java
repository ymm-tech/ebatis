package io.manbang.ebatis.core.response;

import io.manbang.ebatis.core.meta.MethodMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 章多亮
 * @since 2019/12/20 10:35
 */
public class ResponseExtractorLoader {
    private static final Map<MethodMeta, ResponseExtractor<?>> RESPONSE_EXTRACTORS = new ConcurrentHashMap<>();
    private static final List<ResponseExtractorProvider> RESPONSE_EXTRACTOR_PROVIDERS = new ArrayList<>();

    static {
        // 载入全部的提供者，然后排序，用户可以自定义顺序，把自己的Provider优先级提高
        ServiceLoader.load(ResponseExtractorProvider.class).forEach(RESPONSE_EXTRACTOR_PROVIDERS::add);
        Collections.sort(RESPONSE_EXTRACTOR_PROVIDERS);
    }

    private ResponseExtractorLoader() {
        throw new UnsupportedOperationException();
    }

    public static ResponseExtractor<?> getResponseExtractor(MethodMeta method) {
        return RESPONSE_EXTRACTORS.computeIfAbsent(method, ResponseExtractorLoader::findResponseExtractor);
    }

    private static ResponseExtractor<?> findResponseExtractor(MethodMeta method) {
        for (ResponseExtractorProvider provider : RESPONSE_EXTRACTOR_PROVIDERS) {
            if (provider.support(method)) {
                return provider.getResponseExtractor(method);
            }
        }
        throw new UnsupportedOperationException("找不到响应提取器");
    }
}

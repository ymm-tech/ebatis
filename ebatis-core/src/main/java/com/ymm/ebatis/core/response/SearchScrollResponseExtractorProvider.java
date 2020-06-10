package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.annotation.SearchScroll;
import com.ymm.ebatis.core.domain.ScrollResponse;
import com.ymm.ebatis.core.generic.GenericType;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchResponse;

/**
 * @author 章多亮
 * @since 2020/6/8 13:42
 */
@AutoService(ResponseExtractorProvider.class)
public class SearchScrollResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public SearchScrollResponseExtractorProvider() {
        super(RequestType.SEARCH_SCROLL);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> entityClass = genericType.resolve();
        SearchScroll scroll = meta.getAnnotation(SearchScroll.class);
        // 清除
        if (scroll.clearScroll()) {
            if (boolean.class == entityClass || Boolean.class == entityClass) {
                return ClearScrollResponseExtractor.INSTANCE;
            } else if (ClearScrollResponse.class == entityClass) {
                return RawResponseExtractor.INSTANCE;
            }
        }

        if (SearchResponse.class == entityClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (ScrollResponse.class == entityClass) {
            entityClass = genericType.resolveGeneric(0);
            return new ScrollResponseExtractor<>(new DocumentPageExtractor<>(DocumentMapper.of(entityClass)));
        }

        return null;
    }
}

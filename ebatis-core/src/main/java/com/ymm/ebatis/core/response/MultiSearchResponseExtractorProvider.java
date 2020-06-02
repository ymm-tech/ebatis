package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/18 17:25
 */
@AutoService(ResponseExtractorProvider.class)
public class MultiSearchResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public MultiSearchResponseExtractorProvider() {
        super(RequestType.MULTI_SEARCH);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultType = resolvedResultType.resolve();

        if (List.class.isAssignableFrom(resultType)) {
            Class<?> entityClass = resolvedResultType.resolveGeneric(0);
            if (entityClass == Long.class) {
                return TotalHitsListMultiSearchResponseExtractor.INSTANCE;
            } else if (List.class.isAssignableFrom(entityClass)) {
                entityClass = resolvedResultType.resolveGeneric(0, 0);
                return new ListMultiSearchResponseExtractor<>(new DocumentExtractor<>(DocumentMapper.of(entityClass), Integer.MAX_VALUE));
            } else if (Page.class.isAssignableFrom(entityClass)) {
                entityClass = resolvedResultType.resolveGeneric(0, 0);
                return new PageMultiSearchResponseExtractor<>(new DocumentPageExtractor<>(DocumentMapper.of(entityClass)));
            } else if (entityClass.isArray()) {
                entityClass = entityClass.getComponentType();

                if (entityClass == Long.class || entityClass == long.class) {
                    return TotalHitsArrayMultiSearchResponseExtractor.INSTANCE;
                } else {
                    throw new UnsupportedOperationException("不支持");
                }
            } else {
                throw new UnsupportedOperationException("暂不支持的返回值类型");
            }
        } else if (MultiSearchResponse.class == resultType) {
            return TotalHitsArrayMultiSearchResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException("暂不支持的返回值类型");
        }
    }
}

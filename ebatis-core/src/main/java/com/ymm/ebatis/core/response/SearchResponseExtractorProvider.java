package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.domain.Page;
import com.ymm.ebatis.core.meta.MetaUtils;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/17 16:01
 */
@AutoService(ResponseExtractorProvider.class)
public class SearchResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public SearchResponseExtractorProvider() {
        super(RequestType.SEARCH);
    }

    protected SearchResponseExtractorProvider(RequestType requestType) {
        super(requestType);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (SearchResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (Long.class == resultClass || long.class == resultClass) {
            return TotalHitsSearchResponseExtractor.INSTANCE;
        } else if (Page.class.isAssignableFrom(resultClass)) {
            return new DocumentPageExtractor<>(DocumentMapper.of(resolvedResultType.resolveGeneric(0)));
        } else if (List.class.isAssignableFrom(resultClass)) {
            return new DocumentExtractor<>(DocumentMapper.of(resolvedResultType.resolveGeneric(0)), Integer.MAX_VALUE);
        } else if (!MetaUtils.isBasic(resultClass)) {
            return new SingleDocumentExtractor<>(DocumentMapper.of(resultClass));
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

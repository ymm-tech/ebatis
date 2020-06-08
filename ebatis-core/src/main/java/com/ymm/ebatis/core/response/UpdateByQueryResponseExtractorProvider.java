package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:04
 */
@AutoService(ResponseExtractorProvider.class)
public class UpdateByQueryResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public UpdateByQueryResponseExtractorProvider() {
        super(RequestType.UPDATE_BY_QUERY);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();
        if (BulkByScrollResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

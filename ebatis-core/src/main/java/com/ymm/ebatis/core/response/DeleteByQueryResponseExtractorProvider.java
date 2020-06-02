package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.core.ResolvableType;

/**
 * @author 章多亮
 * @since 2020/1/18 14:06
 */
@AutoService(ResponseExtractorProvider.class)
public class DeleteByQueryResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public DeleteByQueryResponseExtractorProvider() {
        super(RequestType.DELETE_BY_QUERY);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (BulkByScrollResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.core.ResolvableType;

import java.util.List;

/**
 * @author 章多亮
 * @since 2020/1/18 17:14
 */
@AutoService(ResponseExtractorProvider.class)
public class BulkResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public BulkResponseExtractorProvider() {
        super(RequestType.BULK);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(ResolvableType resolvedResultType) {
        Class<?> resultClass = resolvedResultType.resolve();

        if (Boolean.class == resultClass || boolean.class == resultClass) {
            return BooleanBulkResponseExtractor.INSTANCE;
        } else if (BulkResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (List.class.isAssignableFrom(resultClass)) {
            return response -> response;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

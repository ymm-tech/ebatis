package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.generic.GenericType;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.meta.RequestType;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;

/**
 * @author 章多亮
 * @since 2020/1/18 13:58
 */
@AutoService(ResponseExtractorProvider.class)
public class DeleteResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public DeleteResponseExtractorProvider() {
        super(RequestType.DELETE);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();

        if (DeleteResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (RestStatus.class == resultClass) {
            return RestStatusResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

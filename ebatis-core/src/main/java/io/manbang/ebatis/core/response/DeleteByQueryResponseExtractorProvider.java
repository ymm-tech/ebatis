package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.rest.RestStatus;

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
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();

        if (DeleteResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (RestStatus.class == resultClass) {
            return response -> ((DeleteResponse) response).status();
        } else if (void.class == resultClass || Void.class == resultClass) {
            return response -> null;
        } else if (DocWriteResponse.Result.class == resultClass) {
            return response -> ((DeleteResponse) response).getResult();
        } else if (Boolean.class == resultClass || boolean.class == resultClass) {
            return response -> ((DeleteResponse) response).getResult() == DocWriteResponse.Result.DELETED;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

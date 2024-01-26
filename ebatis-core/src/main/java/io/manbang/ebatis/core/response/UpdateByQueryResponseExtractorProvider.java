package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateResponse;

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
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (UpdateResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (void.class == resultClass || Void.class == resultClass) {
            return response -> null;
        } else if (DocWriteResponse.Result.class == resultClass) {
            return response -> ((UpdateResponse) response).getResult();
        } else if (Boolean.class == resultClass || boolean.class == resultClass) {
            return response -> ((UpdateResponse) response).getResult() == DocWriteResponse.Result.UPDATED;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

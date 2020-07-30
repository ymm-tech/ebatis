package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;

/**
 * @author 章多亮
 * @since 2020/1/18 11:32
 */
@AutoService(ResponseExtractorProvider.class)
public class UpdateResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public UpdateResponseExtractorProvider() {
        super(RequestType.UPDATE);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();

        if (UpdateResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (GetResult.class == resultClass) {
            return GetResultUpdateResponseExtractor.INSTANCE;
        } else if (RestStatus.class == resultClass) {
            return RestStatusResponseExtractor.INSTANCE;
        } else if (Boolean.class == resultClass || boolean.class == resultClass) {
            return BooleanUpdateResponseExtractor.INSTANCE;
        } else if (Result.class == resultClass) {
            return ResultResponseExtractor.INSTANCE;
        } else if (Void.class == resultClass || void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

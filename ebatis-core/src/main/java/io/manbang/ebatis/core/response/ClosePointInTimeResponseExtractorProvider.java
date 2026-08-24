package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.ClosePointInTimeResponse;

@AutoService(ResponseExtractorProvider.class)
public class ClosePointInTimeResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public ClosePointInTimeResponseExtractorProvider() {
        super(RequestType.CLOSE_POINT_IN_TIME);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (ClosePointInTimeResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (boolean.class == resultClass || Boolean.class == resultClass) {
            return response -> ((ClosePointInTimeResponse) response).isSucceeded();
        } else if (int.class == resultClass || Integer.class == resultClass) {
            return response -> ((ClosePointInTimeResponse) response).getNumFreed();
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported ClosePointInTime return type: " + genericType);
    }
}

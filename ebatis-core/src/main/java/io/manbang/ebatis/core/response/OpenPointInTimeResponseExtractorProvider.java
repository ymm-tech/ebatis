package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.action.search.OpenPointInTimeResponse;

@AutoService(ResponseExtractorProvider.class)
public class OpenPointInTimeResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public OpenPointInTimeResponseExtractorProvider() {
        super(RequestType.OPEN_POINT_IN_TIME);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (OpenPointInTimeResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (String.class == resultClass) {
            return response -> ((OpenPointInTimeResponse) response).getPointInTimeId();
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported OpenPointInTime return type: " + genericType);
    }
}

package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.rest.RestStatus;

@AutoService(ResponseExtractorProvider.class)
public class CountResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public CountResponseExtractorProvider() {
        super(RequestType.COUNT);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (CountResponse.class == resultClass) {
            return response -> ((ValueResponse<CountResponse>) response).getValue();
        } else if (long.class == resultClass || Long.class == resultClass) {
            return response -> ((ValueResponse<CountResponse>) response).getValue().getCount();
        } else if (boolean.class == resultClass || Boolean.class == resultClass) {
            return response -> ((ValueResponse<CountResponse>) response).getValue().getCount() > 0;
        } else if (RestStatus.class == resultClass) {
            return response -> ((ValueResponse<CountResponse>) response).getValue().status();
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported Count return type: " + genericType);
    }
}

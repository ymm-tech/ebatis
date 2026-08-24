package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;

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
        if (BulkByScrollResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (BulkByScrollTask.Status.class == resultClass) {
            return response -> ((BulkByScrollResponse) response).getStatus();
        } else if (ByQueryResponse.class == resultClass) {
            return response -> ByQueryResponse.from((BulkByScrollResponse) response);
        } else if (void.class == resultClass || Void.class == resultClass) {
            return response -> null;
        } else if (Long.class == resultClass || long.class == resultClass) {
            return response -> ((BulkByScrollResponse) response).getUpdated();
        } else if (Boolean.class == resultClass || boolean.class == resultClass) {
            return response -> successful((BulkByScrollResponse) response);
        } else {
            throw new UnsupportedOperationException("Unsupported UpdateByQuery return type: " + genericType);
        }
    }

    private static boolean successful(BulkByScrollResponse response) {
        return !response.isTimedOut()
                && response.getBulkFailures().isEmpty()
                && response.getSearchFailures().isEmpty();
    }
}

package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;

@AutoService(ResponseExtractorProvider.class)
public class ReindexResponseExtractorProvider extends AbstractResponseExtractorProvider {
    public ReindexResponseExtractorProvider() {
        super(RequestType.REINDEX);
    }

    @Override
    protected ResponseExtractor<?> getResponseExtractor(MethodMeta meta, GenericType genericType) {
        Class<?> resultClass = genericType.resolve();
        if (ReindexResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (BulkByScrollResponse.class == resultClass) {
            return response -> ((ReindexResponse) response).getResponse();
        } else if (BulkByScrollTask.Status.class == resultClass) {
            return response -> completed(response).getStatus();
        } else if (ByQueryResponse.class == resultClass) {
            return response -> ByQueryResponse.from(completed(response));
        } else if (String.class == resultClass) {
            return response -> ((ReindexResponse) response).getTaskId();
        } else if (long.class == resultClass || Long.class == resultClass) {
            return response -> completed(response).getCreated() + completed(response).getUpdated();
        } else if (boolean.class == resultClass || Boolean.class == resultClass) {
            return response -> successful((ReindexResponse) response);
        } else if (void.class == resultClass || Void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        }
        throw new UnsupportedOperationException("Unsupported Reindex return type: " + genericType);
    }

    private static BulkByScrollResponse completed(org.elasticsearch.action.ActionResponse response) {
        BulkByScrollResponse result = ((ReindexResponse) response).getResponse();
        if (result == null) {
            throw new IllegalStateException("Reindex was submitted as a task; use String or ReindexResponse return type");
        }
        return result;
    }

    private static boolean successful(ReindexResponse response) {
        if (response.isSubmitted()) {
            return true;
        }
        BulkByScrollResponse result = response.getResponse();
        return !result.isTimedOut() && result.getBulkFailures().isEmpty() && result.getSearchFailures().isEmpty();
    }
}

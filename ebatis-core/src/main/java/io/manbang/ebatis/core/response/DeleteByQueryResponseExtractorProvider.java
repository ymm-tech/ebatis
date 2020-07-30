package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;

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

        if (BulkByScrollResponse.class == resultClass) {
            return RawResponseExtractor.INSTANCE;
        } else if (BulkByScrollTask.Status.class == resultClass) {
            return response -> ((BulkByScrollResponse) response).getStatus();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

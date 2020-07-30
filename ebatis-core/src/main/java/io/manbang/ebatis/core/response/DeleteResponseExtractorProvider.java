package io.manbang.ebatis.core.response;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.generic.GenericType;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.meta.RequestType;
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
        } else if (Boolean.class == resultClass || boolean.class == resultClass) {
            return BooleanDeleteResponseExtractor.INSTANCE;
        } else if (Void.class == resultClass || void.class == resultClass) {
            return VoidResponseExtractor.INSTANCE;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

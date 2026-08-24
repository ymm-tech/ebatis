package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.DocumentExists;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;

class DocumentExistsRequestFactory extends AbstractRequestFactory<DocumentExists, GetRequest> {
    static final DocumentExistsRequestFactory INSTANCE = new DocumentExistsRequestFactory();

    private DocumentExistsRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(GetRequest request, DocumentExists annotation) {
        request.preference(StringUtils.trimToNull(annotation.preference()))
                .refresh(annotation.refresh())
                .realtime(annotation.realtime());
    }

    @Override
    protected GetRequest doCreate(MethodMeta meta, Object[] args) {
        return RequestFactory.get().create(meta, args);
    }
}

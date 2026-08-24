package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.GetSource;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.core.GetSourceRequest;

class GetSourceRequestFactory extends AbstractRequestFactory<GetSource, GetSourceActionRequest> {
    static final GetSourceRequestFactory INSTANCE = new GetSourceRequestFactory();

    private GetSourceRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(GetSourceActionRequest wrapper, GetSource annotation) {
        wrapper.getRequest()
                .preference(StringUtils.trimToNull(annotation.preference()))
                .refresh(annotation.refresh())
                .realtime(annotation.realtime());
    }

    @Override
    protected GetSourceActionRequest doCreate(MethodMeta meta, Object[] args) {
        GetRequest getRequest = RequestFactory.get().create(meta, args);
        return new GetSourceActionRequest(GetSourceRequest.from(getRequest));
    }
}

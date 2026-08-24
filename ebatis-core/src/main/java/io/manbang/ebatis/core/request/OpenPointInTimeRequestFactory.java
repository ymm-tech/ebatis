package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.OpenPointInTime;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.OpenPointInTimeRequest;
import org.elasticsearch.core.TimeValue;

class OpenPointInTimeRequestFactory extends AbstractRequestFactory<OpenPointInTime, OpenPointInTimeRequest> {
    static final OpenPointInTimeRequestFactory INSTANCE = new OpenPointInTimeRequestFactory();

    private OpenPointInTimeRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(OpenPointInTimeRequest request, OpenPointInTime annotation) {
        request.keepAlive(TimeValue.parseTimeValue(annotation.keepAlive(), "OpenPointInTime.keepAlive"))
                .preference(StringUtils.trimToNull(annotation.preference()))
                .routing(StringUtils.trimToNull(annotation.routing()));
    }

    @Override
    protected OpenPointInTimeRequest doCreate(MethodMeta meta, Object[] args) {
        return new OpenPointInTimeRequest(meta.getIndices(meta, args));
    }
}

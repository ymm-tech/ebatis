package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.ClosePointInTime;
import io.manbang.ebatis.core.meta.MethodMeta;
import io.manbang.ebatis.core.provider.PointTimeProvider;
import org.elasticsearch.action.search.ClosePointInTimeRequest;

class ClosePointInTimeRequestFactory extends AbstractRequestFactory<ClosePointInTime, ClosePointInTimeRequest> {
    static final ClosePointInTimeRequestFactory INSTANCE = new ClosePointInTimeRequestFactory();

    private ClosePointInTimeRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(ClosePointInTimeRequest request, ClosePointInTime annotation) {
    }

    @Override
    protected ClosePointInTimeRequest doCreate(MethodMeta meta, Object[] args) {
        Object condition = meta.getConditionParameter().getValue(args);
        String id = condition instanceof PointTimeProvider
                ? ((PointTimeProvider) condition).pitId()
                : String.valueOf(condition);
        return new ClosePointInTimeRequest(id);
    }
}

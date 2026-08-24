package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import org.elasticsearch.action.search.ClosePointInTimeRequest;

class ClosePointInTimeRequestExecutor extends AbstractRequestExecutor<ClosePointInTimeRequest> {
    static final ClosePointInTimeRequestExecutor INSTANCE = new ClosePointInTimeRequestExecutor();

    private ClosePointInTimeRequestExecutor() {
    }

    @Override
    protected RequestAction<ClosePointInTimeRequest> getRequestAction(ClusterSession session) {
        return session::closePointInTimeAsync;
    }

    @Override
    protected RequestFactory<ClosePointInTimeRequest> getRequestFactory() {
        return RequestFactory.closePointInTime();
    }
}

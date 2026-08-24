package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import org.elasticsearch.action.search.OpenPointInTimeRequest;

class OpenPointInTimeRequestExecutor extends AbstractRequestExecutor<OpenPointInTimeRequest> {
    static final OpenPointInTimeRequestExecutor INSTANCE = new OpenPointInTimeRequestExecutor();

    private OpenPointInTimeRequestExecutor() {
    }

    @Override
    protected RequestAction<OpenPointInTimeRequest> getRequestAction(ClusterSession session) {
        return session::openPointInTimeAsync;
    }

    @Override
    protected RequestFactory<OpenPointInTimeRequest> getRequestFactory() {
        return RequestFactory.openPointInTime();
    }
}

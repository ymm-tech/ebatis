package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.ReindexActionRequest;
import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;

class ReindexRequestExecutor extends AbstractRequestExecutor<ReindexActionRequest> {
    static final ReindexRequestExecutor INSTANCE = new ReindexRequestExecutor();

    private ReindexRequestExecutor() {
    }

    @Override
    protected RequestAction<ReindexActionRequest> getRequestAction(ClusterSession session) {
        return session::reindexAsync;
    }

    @Override
    protected RequestFactory<ReindexActionRequest> getRequestFactory() {
        return RequestFactory.reindex();
    }
}

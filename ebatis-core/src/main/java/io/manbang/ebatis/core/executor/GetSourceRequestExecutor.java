package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.GetSourceActionRequest;
import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;

class GetSourceRequestExecutor extends AbstractRequestExecutor<GetSourceActionRequest> {
    static final GetSourceRequestExecutor INSTANCE = new GetSourceRequestExecutor();

    private GetSourceRequestExecutor() {
    }

    @Override
    protected RequestAction<GetSourceActionRequest> getRequestAction(ClusterSession session) {
        return session::getSourceAsync;
    }

    @Override
    protected RequestFactory<GetSourceActionRequest> getRequestFactory() {
        return RequestFactory.getSource();
    }
}

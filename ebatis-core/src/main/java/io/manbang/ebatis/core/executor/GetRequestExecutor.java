package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import org.elasticsearch.action.get.GetRequest;

class GetRequestExecutor extends AbstractRequestExecutor<GetRequest> {
    static final GetRequestExecutor INSTANCE = new GetRequestExecutor();

    private GetRequestExecutor() {
    }

    @Override
    protected RequestAction<GetRequest> getRequestAction(ClusterSession session) {
        return session::getAsync;
    }

    @Override
    protected RequestFactory<GetRequest> getRequestFactory() {
        return RequestFactory.get();
    }
}

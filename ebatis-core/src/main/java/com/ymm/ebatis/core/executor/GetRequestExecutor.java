package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
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

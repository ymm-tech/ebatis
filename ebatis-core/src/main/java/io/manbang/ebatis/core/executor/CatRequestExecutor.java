package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.CatRequest;
import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;

class CatRequestExecutor extends AbstractRequestExecutor<CatRequest> {
    static final CatRequestExecutor INSTANCE = new CatRequestExecutor();

    private CatRequestExecutor() {
    }

    @Override
    protected RequestAction<CatRequest> getRequestAction(ClusterSession session) {
        return session::catAsync;
    }

    @Override
    protected RequestFactory<CatRequest> getRequestFactory() {
        return RequestFactory.cat();
    }
}

package com.ymm.ebatis.executor;

import com.ymm.ebatis.request.CatRequest;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.session.ClusterSession;

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

package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import org.elasticsearch.client.core.CountRequest;

class CountRequestExecutor extends AbstractRequestExecutor<CountRequest> {
    static final CountRequestExecutor INSTANCE = new CountRequestExecutor();

    private CountRequestExecutor() {
    }

    @Override
    protected RequestAction<CountRequest> getRequestAction(ClusterSession session) {
        return session::countAsync;
    }

    @Override
    protected RequestFactory<CountRequest> getRequestFactory() {
        return RequestFactory.count();
    }
}

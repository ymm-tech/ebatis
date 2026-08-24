package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import org.elasticsearch.action.get.GetRequest;

class DocumentExistsRequestExecutor extends AbstractRequestExecutor<GetRequest> {
    static final DocumentExistsRequestExecutor INSTANCE = new DocumentExistsRequestExecutor();

    private DocumentExistsRequestExecutor() {
    }

    @Override
    protected RequestAction<GetRequest> getRequestAction(ClusterSession session) {
        return session::documentExistsAsync;
    }

    @Override
    protected RequestFactory<GetRequest> getRequestFactory() {
        return RequestFactory.documentExists();
    }
}

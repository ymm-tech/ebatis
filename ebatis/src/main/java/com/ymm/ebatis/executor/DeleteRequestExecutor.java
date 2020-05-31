package com.ymm.ebatis.executor;

import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;

/**
 * @author 章多亮
 * @since 2019/12/20 9:31
 */
@Slf4j
class DeleteRequestExecutor extends AbstractRequestExecutor<DeleteRequest> {
    static final RequestExecutor INSTANCE = new DeleteRequestExecutor();

    private DeleteRequestExecutor() {
    }

    @Override
    protected RequestAction<DeleteRequest> getRequestAction(ClusterSession session) {
        return session::deleteAsync;
    }

    @Override
    protected RequestFactory<DeleteRequest> getRequestFactory() {
        return RequestFactory.delete();
    }
}

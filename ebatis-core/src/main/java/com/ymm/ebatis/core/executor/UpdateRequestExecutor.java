package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;

/**
 * @author 章多亮
 * @since 2019/12/19 14:54
 */
@Slf4j
class UpdateRequestExecutor extends AbstractRequestExecutor<UpdateRequest> {
    static final RequestExecutor INSTANCE = new UpdateRequestExecutor();

    private UpdateRequestExecutor() {
    }

    @Override
    protected RequestAction<UpdateRequest> getRequestAction(ClusterSession session) {
        return session::updateAsync;
    }

    @Override
    protected RequestFactory<UpdateRequest> getRequestFactory() {
        return RequestFactory.update();
    }
}

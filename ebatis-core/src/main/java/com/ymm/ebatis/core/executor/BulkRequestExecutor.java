package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;

/**
 * @author 章多亮
 * @since 2019/12/26 19:29
 */
@Slf4j
class BulkRequestExecutor extends AbstractRequestExecutor<BulkRequest> {
    static final BulkRequestExecutor INSTANCE = new BulkRequestExecutor();

    private BulkRequestExecutor() {
    }


    @Override
    protected RequestAction<BulkRequest> getRequestAction(ClusterSession session) {
        return session::bulkAsync;
    }

    @Override
    protected RequestFactory<BulkRequest> getRequestFactory() {
        return RequestFactory.bulk();
    }
}

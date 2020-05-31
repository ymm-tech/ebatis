package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;

/**
 * @author 章多亮
 * @since 2019/12/19 14:52
 */
@Slf4j
class IndexRequestExecutor extends AbstractRequestExecutor<IndexRequest> {
    static final IndexRequestExecutor INSTANCE = new IndexRequestExecutor();

    private IndexRequestExecutor() {
    }

    @Override
    protected RequestAction<IndexRequest> getRequestAction(ClusterSession session) {
        return session::indexAsync;
    }

    @Override
    protected RequestFactory<IndexRequest> getRequestFactory() {
        return RequestFactory.index();
    }
}

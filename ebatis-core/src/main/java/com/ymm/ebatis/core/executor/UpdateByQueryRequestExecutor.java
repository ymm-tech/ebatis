package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;

/**
 * 查询更新方法执行器
 *
 * @author 章多亮
 * @since 2019/12/30 11:51:54
 */
@Slf4j
class UpdateByQueryRequestExecutor extends AbstractRequestExecutor<UpdateByQueryRequest> {
    static final UpdateByQueryRequestExecutor INSTANCE = new UpdateByQueryRequestExecutor();

    private UpdateByQueryRequestExecutor() {
    }

    @Override
    protected RequestAction<UpdateByQueryRequest> getRequestAction(ClusterSession session) {
        return session::updateByQueryAsync;
    }

    @Override
    protected RequestFactory<UpdateByQueryRequest> getRequestFactory() {
        return RequestFactory.updateByQuery();
    }
}

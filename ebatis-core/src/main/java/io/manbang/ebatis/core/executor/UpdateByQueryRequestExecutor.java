package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
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

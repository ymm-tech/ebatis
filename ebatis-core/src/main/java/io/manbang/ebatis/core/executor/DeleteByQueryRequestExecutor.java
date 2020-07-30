package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

/**
 * 查询查询方法执行器
 *
 * @author 章多亮
 * @since 2019/12/28 14:57:53
 */
@Slf4j
class DeleteByQueryRequestExecutor extends AbstractRequestExecutor<DeleteByQueryRequest> {
    static final DeleteByQueryRequestExecutor INSTANCE = new DeleteByQueryRequestExecutor();

    @Override
    protected RequestAction<DeleteByQueryRequest> getRequestAction(ClusterSession session) {
        return session::deleteByQueryAsync;
    }

    @Override
    protected RequestFactory<DeleteByQueryRequest> getRequestFactory() {
        return RequestFactory.deleteByQuery();
    }
}

package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.TriFunction;
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
    protected TriFunction<Cluster, DeleteByQueryRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::deleteByQuery;
    }

    @Override
    protected RequestFactory<DeleteByQueryRequest> getRequestFactory() {
        return RequestFactory.deleteByQuery();
    }
}

package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.common.DslUtils;
import com.ymm.ebatis.core.domain.ResponseExtractor;
import com.ymm.ebatis.core.meta.MethodMeta;
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
class DeleteByQueryMethodExecutor extends AbstractMethodExecutor<DeleteByQueryRequest> {
    static final DeleteByQueryMethodExecutor INSTANCE = new DeleteByQueryMethodExecutor();

    @Override
    protected TriFunction<Cluster, DeleteByQueryRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::deleteByQuery;
    }

    @Override
    protected DeleteByQueryRequest createActionRequest(MethodMeta method, Object[] args) {
        Object condition = method.newCondition(args);

        DeleteByQueryRequest request = method.getRequestFactory().create(MethodMeta.of(method.getMethod()), condition);
        request.getSearchRequest().indices(method.getIndex()).routing(DslUtils.getRouting(method.getRouting()));
        return request;
    }
}

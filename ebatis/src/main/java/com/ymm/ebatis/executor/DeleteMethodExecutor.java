package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.core.domain.ResponseExtractor;
import com.ymm.ebatis.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/20 9:31
 */
@Slf4j
class DeleteMethodExecutor extends AbstractMethodExecutor<DeleteRequest> {
    static final MethodExecutor INSTANCE = new DeleteMethodExecutor();

    private DeleteMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, DeleteRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::delete;
    }

    @Override
    protected DeleteRequest createActionRequest(MethodMeta method, Object[] args) {
        DeleteRequest request = method.getRequestFactory().create(MethodMeta.of(method.getMethod()), args);
        request.index(method.getIndex());

        return request;
    }
}

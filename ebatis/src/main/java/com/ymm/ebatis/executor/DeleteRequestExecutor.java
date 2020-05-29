package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.common.TriFunction;

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
    protected TriFunction<Cluster, DeleteRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::delete;
    }

    @Override
    protected RequestFactory<DeleteRequest> getRequestFactory() {
        return RequestFactory.delete();
    }
}

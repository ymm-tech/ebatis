package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.TriFunction;
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
    protected TriFunction<Cluster, UpdateByQueryRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::updateByQuery;
    }

    @Override
    protected RequestFactory<UpdateByQueryRequest> getRequestFactory() {
        return RequestFactory.updateByQuery();
    }
}

package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.builder.UpdateByQueryRequestFactory;
import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.domain.ResponseExtractor;
import com.ymm.ebatis.core.meta.MethodMeta;
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
public class UpdateByQueryMethodExecutor extends AbstractMethodExecutor<UpdateByQueryRequest> {
    public static final UpdateByQueryMethodExecutor INSTANCE = new UpdateByQueryMethodExecutor();

    private UpdateByQueryMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, UpdateByQueryRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::updateByQuery;
    }

    @Override
    protected UpdateByQueryRequest createActionRequest(MethodMeta method, Object[] args) {
        Object condition = method.newCondition(args);

        UpdateByQueryRequest request = UpdateByQueryRequestFactory.INSTANCE.create(MethodMeta.of(method.getMethod()), new Object[]{condition});
        request.getSearchRequest().indices(method.getIndex());
        return request;
    }
}

package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.domain.ResponseExtractor;
import com.ymm.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/19 14:52
 */
@Slf4j
class IndexMethodExecutor extends AbstractMethodExecutor<IndexRequest> {
    static final IndexMethodExecutor INSTANCE = new IndexMethodExecutor();

    private IndexMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, IndexRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::index;
    }

    @Override
    protected IndexRequest createActionRequest(MethodMeta method, Object[] args) {
        IndexRequest request = method.getRequestFactory().create(MethodMeta.of(method.getMethod()), args);
        request.index(method.getIndex());

        return request;
    }
}

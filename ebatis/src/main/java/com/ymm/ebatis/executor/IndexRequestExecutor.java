package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/19 14:52
 */
@Slf4j
class IndexRequestExecutor extends AbstractRequestExecutor<IndexRequest> {
    static final IndexRequestExecutor INSTANCE = new IndexRequestExecutor();

    private IndexRequestExecutor() {
    }

    @Override
    protected TriFunction<Cluster, IndexRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::index;
    }

    @Override
    protected IndexRequest createActionRequest(MethodMeta meta, Object[] args) {
        IndexRequest request = getRequestFactory().create(meta, args);
        request.index(meta.getIndex());

        return request;
    }

    @Override
    protected RequestFactory<IndexRequest> getRequestFactory() {
        return RequestFactory.index();
    }
}

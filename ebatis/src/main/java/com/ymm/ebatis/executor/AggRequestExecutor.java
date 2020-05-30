package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2020/1/2 20:22
 */
class AggRequestExecutor extends AbstractRequestExecutor<SearchRequest> {
    static final AggRequestExecutor INSTANCE = new AggRequestExecutor();

    private AggRequestExecutor() {
    }

    @Override
    protected RequestFactory<SearchRequest> getRequestFactory() {
        return RequestFactory.agg();
    }

    @Override
    protected TriFunction<Cluster, SearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::search;
    }
}

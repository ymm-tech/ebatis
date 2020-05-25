package com.ymm.ebatis.executor;

import com.ymm.ebatis.annotation.Agg;
import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.core.domain.ResponseExtractor;
import com.ymm.ebatis.meta.MethodMeta;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2020/1/2 20:22
 */
public class AggMethodExecutor extends AbstractMethodExecutor<SearchRequest> {
    public static final AggMethodExecutor INSTANCE = new AggMethodExecutor();

    private AggMethodExecutor() {
    }

    @Override
    protected SearchRequest createActionRequest(MethodMeta method, Object[] args) {
        Agg agg = method.getAnnotation(Agg.class);

        SearchRequest request = agg.type().getFactory().create(MethodMeta.of(method.getMethod()), args);
        request.indices(method.getIndex());

        return request;
    }

    @Override
    protected TriFunction<Cluster, SearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::search;
    }
}

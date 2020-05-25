package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/19 13:45
 */
@Slf4j
class SearchMethodExecutor extends AbstractMethodExecutor<SearchRequest> {
    static final MethodExecutor INSTANCE = new SearchMethodExecutor();

    protected SearchMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, SearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::search;
    }

    @Override
    protected SearchRequest createActionRequest(MethodMeta method, Object[] args) {
        Object condition = method.newCondition(args);
        RequestFactory requestFactory = method.getRequestFactory();
        SearchRequest request = requestFactory.create(MethodMeta.of(method.getMethod()), condition);
        request.indices(method.getIndex());

        if (!(condition instanceof SourceProvider) && !method.isVoidReturnType()) {
            request.source().fetchSource(method.getIncludeFields(), ArrayUtils.EMPTY_STRING_ARRAY);
        }
        return request;
    }
}

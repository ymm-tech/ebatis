package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2019/12/19 13:45
 */
@Slf4j
class SearchRequestExecutor extends AbstractRequestExecutor<SearchRequest> {
    static final RequestExecutor INSTANCE = new SearchRequestExecutor();

    protected SearchRequestExecutor() {
    }

    @Override
    protected TriFunction<Cluster, SearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::search;
    }

    @Override
    protected RequestFactory<SearchRequest> getRequestFactory() {
        return RequestFactory.search();
    }
}

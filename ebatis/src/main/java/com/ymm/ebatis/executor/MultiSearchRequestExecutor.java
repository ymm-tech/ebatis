package com.ymm.ebatis.executor;

import com.ymm.ebatis.cluster.Cluster;
import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.request.RequestFactory;
import com.ymm.ebatis.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2020/1/14 16:18
 */
@Slf4j
class MultiSearchRequestExecutor extends AbstractRequestExecutor<MultiSearchRequest> {
    static final MultiSearchRequestExecutor INSTANCE = new MultiSearchRequestExecutor();

    private MultiSearchRequestExecutor() {
    }

    @Override
    protected TriFunction<Cluster, MultiSearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::multiSearch;
    }

    @Override
    protected RequestFactory<MultiSearchRequest> getRequestFactory() {
        return RequestFactory.multiSearch();
    }
}

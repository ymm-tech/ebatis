package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.cluster.Cluster;
import com.ymm.ebatis.core.meta.MethodMeta;
import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.response.ResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.common.TriFunction;

/**
 * @author 章多亮
 * @since 2020/1/14 16:18
 */
@Slf4j
public class MultiSearchMethodExecutor extends AbstractMethodExecutor<MultiSearchRequest> {
    public static final MultiSearchMethodExecutor INSTANCE = new MultiSearchMethodExecutor();

    private MultiSearchMethodExecutor() {
    }

    @Override
    protected TriFunction<Cluster, MultiSearchRequest, ResponseExtractor<?>, Object> getExecutor(MethodMeta method) {
        return method.getResultType()::multiSearch;
    }

    @Override
    protected MultiSearchRequest createActionRequest(MethodMeta method, Object[] args) {
        Object condition = method.newCondition(args);

        RequestFactory requestFactory = method.getRequestFactory();
        MultiSearchRequest multiSearchRequest = requestFactory.create(MethodMeta.of(method.getMethod()), condition);
        multiSearchRequest.requests().forEach(request -> {
            request.indices(method.getIndex());
            if (!(condition instanceof SourceProvider) && !method.isVoidReturnType()) {
                request.source().fetchSource(method.getIncludeFields(), ArrayUtils.EMPTY_STRING_ARRAY);
            }
        });

        return multiSearchRequest;
    }
}

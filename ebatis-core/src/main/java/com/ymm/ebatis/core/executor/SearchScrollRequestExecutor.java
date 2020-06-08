package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchScrollRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author 章多亮
 * @since 2020/6/8 9:47
 */
class SearchScrollRequestExecutor extends AbstractRequestExecutor<ActionRequest> {
    static final SearchScrollRequestExecutor INSTANCE = new SearchScrollRequestExecutor();

    private SearchScrollRequestExecutor() {
    }

    @Override
    protected RequestAction<ActionRequest> getRequestAction(ClusterSession session) {
        return (request, extractor) -> {
            if (request instanceof SearchRequest) {
                return session.searchAsync((SearchRequest) request, extractor);
            } else if (request instanceof SearchScrollRequest) {
                return session.scrollAsync((SearchScrollRequest) request, extractor);
            } else if (request instanceof ClearScrollRequest) {
                return session.clearScrollAsync((ClearScrollRequest) request, extractor);
            } else {
                return CompletableFuture.completedFuture(null);
            }
        };
    }

    @Override
    protected RequestFactory<ActionRequest> getRequestFactory() {
        return RequestFactory.searchScroll();
    }
}

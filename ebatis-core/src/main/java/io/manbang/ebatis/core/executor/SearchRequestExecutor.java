package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;

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
    protected RequestAction<SearchRequest> getRequestAction(ClusterSession session) {
        return session::searchAsync;
    }


    @Override
    protected RequestFactory<SearchRequest> getRequestFactory() {
        return RequestFactory.search();
    }
}

package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;

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
    protected RequestAction<MultiSearchRequest> getRequestAction(ClusterSession session) {
        return session::multiSearchAsync;
    }

    @Override
    protected RequestFactory<MultiSearchRequest> getRequestFactory() {
        return RequestFactory.multiSearch();
    }
}

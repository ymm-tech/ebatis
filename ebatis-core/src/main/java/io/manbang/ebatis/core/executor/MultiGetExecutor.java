package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.session.ClusterSession;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.MultiGetRequest;

/**
 * @author weilong.hu
 * @since 2020/7/2 10:31
 */
@Slf4j
public class MultiGetExecutor extends AbstractRequestExecutor<MultiGetRequest> {
    static final MultiGetExecutor INSTANCE = new MultiGetExecutor();

    private MultiGetExecutor() {

    }

    @Override
    protected RequestAction<MultiGetRequest> getRequestAction(ClusterSession session) {
        return session::mgetAsync;
    }

    @Override
    protected RequestFactory<MultiGetRequest> getRequestFactory() {
        return RequestFactory.multiGet();
    }
}

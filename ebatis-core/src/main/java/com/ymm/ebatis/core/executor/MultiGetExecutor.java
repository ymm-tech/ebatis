package com.ymm.ebatis.core.executor;

import com.ymm.ebatis.core.request.RequestFactory;
import com.ymm.ebatis.core.session.ClusterSession;
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

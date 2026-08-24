package io.manbang.ebatis.core.executor;

import io.manbang.ebatis.core.request.RequestFactory;
import io.manbang.ebatis.core.request.TaskResultActionRequest;
import io.manbang.ebatis.core.session.ClusterSession;

class TaskResultRequestExecutor extends AbstractRequestExecutor<TaskResultActionRequest> {
    static final TaskResultRequestExecutor INSTANCE = new TaskResultRequestExecutor();

    private TaskResultRequestExecutor() {
    }

    @Override
    protected RequestAction<TaskResultActionRequest> getRequestAction(ClusterSession session) {
        return session::taskResultAsync;
    }

    @Override
    protected RequestFactory<TaskResultActionRequest> getRequestFactory() {
        return RequestFactory.taskResult();
    }
}

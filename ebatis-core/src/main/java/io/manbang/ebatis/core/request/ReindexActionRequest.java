package io.manbang.ebatis.core.request;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.index.reindex.ReindexRequest;

public final class ReindexActionRequest extends ActionRequest {
    private final ReindexRequest request;
    private final boolean waitForCompletion;

    public ReindexActionRequest(ReindexRequest request, boolean waitForCompletion) {
        this.request = request;
        this.waitForCompletion = waitForCompletion;
    }

    public ReindexRequest getRequest() {
        return request;
    }

    public boolean isWaitForCompletion() {
        return waitForCompletion;
    }

    @Override
    public ActionRequestValidationException validate() {
        return request.validate();
    }
}

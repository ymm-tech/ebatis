package io.manbang.ebatis.core.response;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.reindex.BulkByScrollResponse;

import java.io.IOException;

public final class ReindexResponse extends ActionResponse {
    private final BulkByScrollResponse response;
    private final String taskId;

    public ReindexResponse(BulkByScrollResponse response) {
        this.response = response;
        this.taskId = null;
    }

    public ReindexResponse(String taskId) {
        this.response = null;
        this.taskId = taskId;
    }

    public BulkByScrollResponse getResponse() {
        return response;
    }

    public String getTaskId() {
        return taskId;
    }

    public boolean isSubmitted() {
        return taskId != null;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        // Local REST client adapter response.
    }
}

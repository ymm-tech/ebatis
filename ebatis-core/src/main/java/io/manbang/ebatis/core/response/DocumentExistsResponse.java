package io.manbang.ebatis.core.response;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public final class DocumentExistsResponse extends ActionResponse {
    private final boolean exists;

    public DocumentExistsResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        // Local REST client adapter response.
    }
}

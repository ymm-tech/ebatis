package io.manbang.ebatis.core.response;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * Adapts high-level client responses that do not extend {@link ActionResponse}.
 */
public final class ValueResponse<T> extends ActionResponse {
    private final T value;

    public ValueResponse(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        // This response only exists inside the REST client adapter and is never transported.
    }
}

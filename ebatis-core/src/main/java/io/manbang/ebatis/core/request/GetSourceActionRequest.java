package io.manbang.ebatis.core.request;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.client.core.GetSourceRequest;

public final class GetSourceActionRequest extends ActionRequest {
    private final GetSourceRequest request;

    public GetSourceActionRequest(GetSourceRequest request) {
        this.request = request;
    }

    public GetSourceRequest getRequest() {
        return request;
    }

    @Override
    public ActionRequestValidationException validate() {
        return request.validate().map(validation -> {
            ActionRequestValidationException exception = new ActionRequestValidationException();
            validation.validationErrors().forEach(exception::addValidationError);
            return exception;
        }).orElse(null);
    }
}

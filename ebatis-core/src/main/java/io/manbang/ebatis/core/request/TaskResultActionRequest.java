package io.manbang.ebatis.core.request;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.client.tasks.GetTaskRequest;

public final class TaskResultActionRequest extends ActionRequest {
    private final GetTaskRequest request;

    public TaskResultActionRequest(GetTaskRequest request) {
        this.request = request;
    }

    public GetTaskRequest getRequest() {
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

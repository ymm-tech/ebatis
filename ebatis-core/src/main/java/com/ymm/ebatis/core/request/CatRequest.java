package com.ymm.ebatis.core.request;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.client.Request;

public abstract class CatRequest extends ActionRequest implements RequestConverter {
    @Override
    public ActionRequestValidationException validate() {
        return null;
    }

    @Override
    public Request toRequest() {
        Request request = new Request("GET", "/_cat" + getSubEndpoint());
        customize(request);
        return request;
    }

    protected abstract String getSubEndpoint();

    protected abstract void customize(Request request);

    @Override
    public String toString() {
        return toRequest().toString();
    }
}

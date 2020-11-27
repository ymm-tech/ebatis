package io.manbang.ebatis.core.interceptor;

import org.elasticsearch.action.ActionRequest;

/**
 * @author weilong.hu
 * @since 2020-04-22
 */
public class DefaultRequestInfo<T extends ActionRequest> implements RequestInfo<T> {
    private T request;
    private Object[] args;

    public DefaultRequestInfo(T actionRequest, Object[] args) {
        this.request = actionRequest;
        this.args = args;
    }

    @Override
    public T actionRequest() {
        return request;
    }

    @Override
    public Object[] args() {
        return args;
    }

    @Override
    public String toString() {
        return request.toString();
    }
}
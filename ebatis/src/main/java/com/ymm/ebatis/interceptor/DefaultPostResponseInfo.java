package com.ymm.ebatis.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @date 2020-04-22
 */
public class DefaultPostResponseInfo<T extends ActionRequest, R extends ActionResponse> implements PostResponseInfo<T, R> {

    private T request;
    private R response;

    public DefaultPostResponseInfo(T actionRequest, R actionResponse) {
        this.request = actionRequest;
        this.response = actionResponse;
    }

    @Override
    public T actionRequest() {
        return request;
    }

    @Override
    public R actionResponse() {
        return response;
    }
}

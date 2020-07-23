package com.ymm.ebatis.core.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

import java.util.List;

/**
 * @author weilong.hu
 * @date 2020-04-22
 */
public class Interceptors implements Interceptor {
    private List<Interceptor> interceptors;

    public Interceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void handleException(Throwable throwable) {
        interceptors.forEach(i -> i.handleException(throwable));
    }

    @Override
    public void preRequest(Object[] args) {
        interceptors.forEach(i -> i.preRequest(args));
    }

    @Override
    public <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
        interceptors.forEach(i -> i.postRequest(requestInfo));
    }

    @Override
    public <T extends ActionRequest> void preResponse(PreResponseInfo<T> preResponseInfo) {
        interceptors.forEach(i -> i.preResponse(preResponseInfo));
    }

    @Override
    public <T extends ActionRequest, R extends ActionResponse> void postResponse(PostResponseInfo<T, R> postResponseInfo) {
        interceptors.forEach(i -> i.postResponse(postResponseInfo));
    }
}

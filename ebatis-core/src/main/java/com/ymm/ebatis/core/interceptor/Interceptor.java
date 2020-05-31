package com.ymm.ebatis.core.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @date 2020-04-21
 */
public interface Interceptor {
    /**
     * 拦截器顺序，越小优先级越高
     *
     * @return 优先级
     */
    int getOrder();

    /**
     * 异常处理
     *
     * @param throwable ebatis处理过程中出现的异常
     */
    default void handleException(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 入参拼装请求之前
     */
    default void preRequest(Object[] args) {
    }

    /**
     * 入参拼装请求之后
     */
    default <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
    }

    /**
     * 请求发送之前
     */
    default <T extends ActionRequest, R extends ActionResponse> void preResponse(PreResponseInfo<T, R> preResponseInfo) {
    }

    /**
     * 请求响应之后
     */
    default <T extends ActionRequest, R extends ActionResponse> void postResponse(PostResponseInfo<T, R> postResponseInfo) {
    }
}

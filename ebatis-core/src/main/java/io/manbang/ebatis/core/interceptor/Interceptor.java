package io.manbang.ebatis.core.interceptor;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @since 2020-04-21
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
     *
     * @param args    args
     * @param cluster cluster
     * @param meta    meta
     */
    default void preRequest(Object[] args, Cluster cluster, MethodMeta meta) {
    }

    /**
     * 入参拼装请求之后
     * @param requestInfo request
     * @param <T> T extends ActionRequest
     */
    default <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
    }

    /**
     * 请求发送之前
     * @param preResponseInfo response info
     * @param <T> T extends ActionRequest
     */
    default <T extends ActionRequest> void preResponse(PreResponseInfo<T> preResponseInfo) {
    }

    /**
     * 请求响应之后
     * @param postResponseInfo post response
     * @param <T> T extends ActionRequest
     * @param <R> R extends ActionResponse
     *
     */
    default <T extends ActionRequest, R extends ActionResponse> void postResponse(PostResponseInfo<T, R> postResponseInfo) {
    }
}

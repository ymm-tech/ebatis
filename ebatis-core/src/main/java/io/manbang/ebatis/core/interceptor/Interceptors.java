package io.manbang.ebatis.core.interceptor;

import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.meta.MethodMeta;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

import java.util.List;

/**
 * @author weilong.hu
 * @since 2020-04-22
 */
public class Interceptors implements Interceptor {
    private final List<Interceptor> chainedInterceptors;

    public Interceptors(List<Interceptor> interceptors) {
        this.chainedInterceptors = interceptors;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void handleException(Throwable throwable) {
        chainedInterceptors.forEach(i -> i.handleException(throwable));
    }

    @Override
    public void preRequest(Object[] args, Cluster cluster, MethodMeta meta) {
        chainedInterceptors.forEach(i -> i.preRequest(args, cluster, meta));
    }

    @Override
    public <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
        chainedInterceptors.forEach(i -> i.postRequest(requestInfo));
    }

    @Override
    public <T extends ActionRequest> void preResponse(PreResponseInfo<T> preResponseInfo) {
        chainedInterceptors.forEach(i -> i.preResponse(preResponseInfo));
    }

    @Override
    public <T extends ActionRequest, R extends ActionResponse> void postResponse(PostResponseInfo<T, R> postResponseInfo) {
        chainedInterceptors.forEach(i -> i.postResponse(postResponseInfo));
    }
}

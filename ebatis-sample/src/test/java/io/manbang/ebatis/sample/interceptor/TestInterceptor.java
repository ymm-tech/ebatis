package io.manbang.ebatis.sample.interceptor;

import com.google.auto.service.AutoService;
import io.manbang.ebatis.core.cluster.Cluster;
import io.manbang.ebatis.core.domain.ContextHolder;
import io.manbang.ebatis.core.interceptor.Interceptor;
import io.manbang.ebatis.core.interceptor.PostResponseInfo;
import io.manbang.ebatis.core.interceptor.PreResponseInfo;
import io.manbang.ebatis.core.interceptor.RequestInfo;
import io.manbang.ebatis.core.meta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * z
 *
 * @author weilong.hu
 * @since 2020/6/17 17:30
 */
@Slf4j
@AutoService(Interceptor.class)
public class TestInterceptor implements Interceptor {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void handleException(Throwable throwable) {
        log.error("tete", throwable);
        log.error("throwable userId{}", ContextHolder.getString("userId"));
    }

    @Override
    public void preRequest(Object[] args, Cluster cluster, MethodMeta meta) {
        log.info("preRequest", args);
        log.error("preRequest userId{}", ContextHolder.getString("userId"));

    }

    @Override
    public <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
        log.info("requestInfo1{}", requestInfo);
        log.info("requestInfo2{}", requestInfo.actionRequest());
        log.error("requestInfo userId{}", ContextHolder.getString("userId"));

    }

    @Override
    public <T extends ActionRequest> void preResponse(PreResponseInfo<T> preResponseInfo) {
        log.error("preResponseInfo userId{}", ContextHolder.getString("userId"));

    }

    @Override
    public <T extends ActionRequest, R extends ActionResponse> void postResponse(PostResponseInfo<T, R> postResponseInfo) {
        log.info("postResponseInfo{}", postResponseInfo.actionResponse());
        log.error("postResponseInfo userId{}", ContextHolder.<String>getValue("userId"));

    }
}

package com.ymm.ebatis.interceptor;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionRequest;

@Slf4j
@AutoService(Interceptor.class)
public class LogInterceptor implements Interceptor {
    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public <T extends ActionRequest> void postRequest(RequestInfo<T> requestInfo) {
        log.info("{}", requestInfo);
    }
}

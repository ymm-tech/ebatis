package com.ymm.ebatis.core.interceptor;

import com.ymm.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @date 2020-04-21
 */
public interface PreResponseInfo<T extends ActionRequest, R extends ActionResponse> {
    /**
     * @return
     */
    T actionRequest();

    Class<R> responseClass();

    ResponseExtractor<?> extractor();
}

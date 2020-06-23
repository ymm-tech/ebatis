package com.ymm.ebatis.core.interceptor;

import com.ymm.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionRequest;

/**
 * @author weilong.hu
 * @date 2020-04-21
 */
public interface PreResponseInfo<T extends ActionRequest> {
    /**
     * 返回ActionRequest
     *
     * @return actionRequest
     */
    T actionRequest();

    ResponseExtractor<?> extractor();
}

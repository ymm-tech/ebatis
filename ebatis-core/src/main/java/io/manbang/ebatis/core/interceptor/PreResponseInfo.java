package io.manbang.ebatis.core.interceptor;

import io.manbang.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionRequest;

/**
 * @author weilong.hu
 * @since 2020-04-21
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

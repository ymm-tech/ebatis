package io.manbang.ebatis.core.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @date 2020-04-21
 */
public interface PostResponseInfo<T extends ActionRequest, R extends ActionResponse> {
    /**
     * 获取响应体
     *
     * @return 响应体
     */
    @Override
    String toString();

    /**
     * @return
     */
    T actionRequest();

    /**
     *
     */
    R actionResponse();

}

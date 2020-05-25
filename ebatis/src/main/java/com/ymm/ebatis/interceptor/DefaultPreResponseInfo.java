package com.ymm.ebatis.interceptor;

import com.ymm.ebatis.response.ResponseExtractor;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;

/**
 * @author weilong.hu
 * @date 2020-04-22
 */
public class DefaultPreResponseInfo<T extends ActionRequest, R extends ActionResponse> implements PreResponseInfo<T, R> {
    private T request;
    private Class<R> responseClass;
    private ResponseExtractor<?> extractor;

    public DefaultPreResponseInfo(T actionRequest, Class<R> responseClass, ResponseExtractor<?> extractor) {
        this.request = actionRequest;
        this.responseClass = responseClass;
        this.extractor = extractor;
    }

    @Override
    public T actionRequest() {
        return request;
    }

    @Override
    public Class<R> responseClass() {
        return responseClass;
    }

    @Override
    public ResponseExtractor<?> extractor() {
        return extractor;
    }
}

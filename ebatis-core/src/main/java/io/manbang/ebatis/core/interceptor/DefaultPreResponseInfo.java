package io.manbang.ebatis.core.interceptor;

import io.manbang.ebatis.core.response.ResponseExtractor;
import org.elasticsearch.action.ActionRequest;

/**
 * @author weilong.hu
 * @since 2020-04-22
 */
public class DefaultPreResponseInfo<T extends ActionRequest> implements PreResponseInfo<T> {
    private T request;
    private ResponseExtractor<?> extractor;

    public DefaultPreResponseInfo(T actionRequest, ResponseExtractor<?> extractor) {
        this.request = actionRequest;
        this.extractor = extractor;
    }

    @Override
    public T actionRequest() {
        return request;
    }


    @Override
    public ResponseExtractor<?> extractor() {
        return extractor;
    }
}

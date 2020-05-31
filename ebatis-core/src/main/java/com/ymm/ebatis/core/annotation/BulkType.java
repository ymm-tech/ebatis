package com.ymm.ebatis.core.annotation;

import com.ymm.ebatis.core.request.RequestFactory;
import org.elasticsearch.action.ActionRequest;

/**
 * @author 章多亮
 * @since 2019/12/26 19:43
 */
public enum BulkType {
    /**
     * 索引
     */
    INDEX(RequestFactory.index()),
    /**
     * 删除
     */
    DELETE(RequestFactory.delete()),
    /**
     * 更新
     */
    UPDATE(RequestFactory.update());

    private final RequestFactory<?> requestFactory;

    <R extends ActionRequest> BulkType(RequestFactory<R> requestFactory) {
        this.requestFactory = requestFactory;
    }

    @SuppressWarnings("unchecked")
    public <R extends ActionRequest> RequestFactory<R> getRequestFactory() {
        return (RequestFactory<R>) requestFactory;
    }
}

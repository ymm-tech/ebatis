package com.ymm.ebatis.core.annotation;

import com.ymm.ebatis.core.request.CatRequest;
import com.ymm.ebatis.core.request.RequestFactory;

public enum CatType {
    /**
     * 别名
     */
    ALIASES(RequestFactory.catAliases()),
    /**
     *
     */
    ALLOCATION(null),
    /**
     * 统计文档数量
     */
    COUNT(RequestFactory.catCount()),
    ;

    private final RequestFactory<? extends CatRequest> requestFactory;

    CatType(RequestFactory<? extends CatRequest> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public RequestFactory<? extends CatRequest> getRequestFactory() {
        return requestFactory;
    }
}

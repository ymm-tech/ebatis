package com.ymm.ebatis.core.annotation;

import com.ymm.ebatis.core.request.RequestFactory;
import org.elasticsearch.action.search.SearchRequest;

/**
 * 聚合类型
 *
 * @author 章多亮
 * @since 2020/1/2 16:43
 */
public enum AggType {
    /**
     * 指标聚合
     */
    METRIC(RequestFactory.metric()),
    /**
     * 桶聚合
     */
    BUCKET(RequestFactory.bucket()),
    /**
     * 管道聚合
     */
    PIPELINE(RequestFactory.pipeline()),
    /**
     * 矩阵聚合
     */
    MATRIX(RequestFactory.matrix());
    private final RequestFactory<SearchRequest> requestFactory;

    AggType(RequestFactory<SearchRequest> requestFactory) {
        this.requestFactory = requestFactory;
    }

    public final RequestFactory<SearchRequest> getRequestFactory() {
        return requestFactory;
    }
}

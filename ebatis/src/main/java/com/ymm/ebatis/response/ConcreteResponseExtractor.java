package com.ymm.ebatis.response;

import org.elasticsearch.action.ActionResponse;

/**
 * @author duoliang.zhang
 */
public interface ConcreteResponseExtractor<T, R extends ActionResponse> extends ResponseExtractor<T> {
    /**
     * 抽提ES响应转换为实体对象
     *
     * @param response 响应
     * @return 实体对象
     */
    @Override
    @SuppressWarnings("unchecked")
    default T extractData(ActionResponse response) {
        return doExtractData((R) response);
    }

    /**
     * 抽提实际响应的数据
     *
     * @param response 具体响应
     * @return 实体数据
     */
    T doExtractData(R response);
}

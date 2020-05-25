package com.ymm.ebatis.response;

import org.elasticsearch.action.ActionResponse;

/**
 * 简单响应处理器，返回值直接返回
 *
 * @param <R> 响应类型
 * @author 章多亮
 * @since 2019/12/28 17:04:05
 */
public class SimpleResponseExtractor<R extends ActionResponse> implements ConcreteResponseExtractor<R, R> {
    @Override
    public R doExtractData(R response) {
        return response;
    }
}

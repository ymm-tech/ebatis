package io.manbang.ebatis.core.interceptor;

import org.elasticsearch.action.ActionRequest;

/**
 * @author weilong.hu
 * @since 2020-04-21
 */
public interface RequestInfo<T extends ActionRequest> {
    /**
     * 获取DSL语句
     *
     * @return DSL语句
     */
    @Override
    String toString();

    /**
     * 获取actionRequest
     *
     * @return T
     */
    T actionRequest();

    /**
     * 入参
     *
     * @return object[]
     */
    Object[] args();


}

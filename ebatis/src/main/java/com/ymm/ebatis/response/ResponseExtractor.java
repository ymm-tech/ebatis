package com.ymm.ebatis.response;

import org.elasticsearch.action.ActionResponse;

/**
 * @author 章多亮
 * @since 2019/12/24 11:58
 */
@FunctionalInterface
public interface ResponseExtractor<T> {
    /**
     * 提取响应，转为是业务实体
     *
     * @param response 响应
     * @return 实体
     */
    T extractData(ActionResponse response);

    /**
     * 验证响应是否正确
     *
     * @param response 响应返回值
     * @return 如果响应正常，返回<code>true</code>
     */
    default boolean validate(ActionResponse response) {
        return response != null;
    }

    /**
     * 用于调试，不实际调用ES
     *
     * @return 空值
     */
    default T empty() {
        return null;
    }

    /**
     * 异常处理，如果降级使能的话，此方法就不调用，直接调动 {@link ResponseExtractor#fallback(Exception)}
     *
     * @param ex 任意异常
     */
    default void catchException(Exception ex) {
        // do nothing default
    }

    /**
     * 判断提取器是否启用fallback
     *
     * @return 如果启用fallback，返回<code>true</code>
     */
    default boolean fallbackEnabled() {
        return false;
    }

    /**
     * 如果响应有异常，执行降级操作
     *
     * @param ex 降级是发生的异常
     * @return 降级返回值
     */
    default T fallback(Exception ex) {
        return null;
    }
}

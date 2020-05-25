package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.MethodMeta;
import com.ymm.ebatis.meta.RequestType;
import com.ymm.ebatis.meta.ResultType;

/**
 * 响应提取器提供者，要根据返回类型 {@link ResultType 结果类型} 和 {@link RequestType 请求类型}，综合判断返回什么想的响应提取器
 *
 * @author 章多亮
 * @since 2020/1/17 11:59
 */
public interface ResponseExtractorProvider {
    /**
     * 判断当前Provider是否支持提取当前方法的返回值
     *
     * @param method Mapper方法
     * @return 如果支持提取此方法的返回值，返回<code>true</code>
     */
    boolean support(MethodMeta method);

    /**
     * 获取指定方法的响应提取器
     *
     * @param method Mapper方法
     * @return 响应提取器
     */
    ResponseExtractor<?> getResponseExtractor(MethodMeta method);
}

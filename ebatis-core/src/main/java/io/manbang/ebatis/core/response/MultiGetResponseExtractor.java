package io.manbang.ebatis.core.response;

import org.elasticsearch.action.get.MultiGetResponse;

/**
 * @author weilong.hu
 */
public interface MultiGetResponseExtractor<T> extends ConcreteResponseExtractor<T, MultiGetResponse> {
    /**
     * 通过实体类型创建MultiGet提取器
     *
     * @param entityClass 实体对象
     * @param <T>         实体对象类型
     * @return MultiGet提取器
     */
    static <T> ArrayMultiGetResponseExtractor<T> of(Class<T> entityClass) {
        return ArrayMultiGetResponseExtractor.of(entityClass);
    }
}

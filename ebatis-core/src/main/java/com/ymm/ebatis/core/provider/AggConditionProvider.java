package com.ymm.ebatis.core.provider;

/**
 * 聚合条件提供者
 *
 * @author 章多亮
 * @since 2020/1/2 19:51
 */
public interface AggConditionProvider extends Provider {
    /**
     * 获取所搜条件
     *
     * @return 搜索条件
     */
    default Object getCondition() {
        return this;
    }
}

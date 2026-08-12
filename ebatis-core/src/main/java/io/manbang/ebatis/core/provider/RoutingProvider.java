package io.manbang.ebatis.core.provider;

/**
 * 路由
 *
 * @author weilong.hu
 */
public interface RoutingProvider extends Provider {
    /**
     * 获取routing值
     *
     * @return 字段列表
     */
    String routing();
}

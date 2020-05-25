package com.ymm.ebatis.cluster;

/**
 * 权重化的
 *
 * @author 章多亮
 * @since 2019/12/18 10:23
 */
public interface Weighted {
    /**
     * 获取权重值
     *
     * @return 权重值
     */
    int getWeight();
}

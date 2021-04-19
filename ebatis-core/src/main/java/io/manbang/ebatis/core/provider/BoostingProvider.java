package io.manbang.ebatis.core.provider;

/**
 * @author weilong.hu
 * @since 2021/4/19 18:41
 */
public interface BoostingProvider extends Provider {
    /**
     * 获取匹配条件
     *
     * @return 匹配条件
     */
    Object positive();

    /**
     * 获取降分条件
     *
     * @return 降分条件
     */
    Object negative();

    /**
     * 获取降分系数
     *
     * @return 降分系数
     */
    default float negativeBoost() {
        return 1;
    }
}

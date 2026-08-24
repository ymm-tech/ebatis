package io.manbang.ebatis.core.provider;

public interface PointTimeProvider extends Provider {
    /**
     * 获取point time id
     *
     * @return point time id
     */
    String pitId();

    /**
     * 每次搜索对 PIT 续期；空值时沿用服务端当前值。
     */
    default String keepAlive() {
        return "";
    }
}

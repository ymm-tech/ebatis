package io.manbang.ebatis.core.provider;

/**
 * 版本提供器，如果实现的版本控制器，则默认会设置 {@link io.manbang.ebatis.core.annotation.VersionType#EXTERNAL}
 *
 * @author 章多亮
 * @since 2019/12/26 14:25
 */
public interface VersionProvider extends Provider {
    /**
     * 获取当前操作的版本
     *
     * @return 版本
     */
    long version();
}

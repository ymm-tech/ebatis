package com.ymm.ebatis.provider;

/**
 * 版本提供器
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
    long getVersion();
}

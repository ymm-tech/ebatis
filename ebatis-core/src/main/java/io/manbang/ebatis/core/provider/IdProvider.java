package io.manbang.ebatis.core.provider;

/**
 * @author 章多亮
 * @since 2020/5/29 10:12
 */
public interface IdProvider extends Provider {
    /**
     * 提取文档Id
     *
     * @return 文档Id
     */
    String id();
}

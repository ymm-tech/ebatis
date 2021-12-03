package io.manbang.ebatis.core.provider;

/**
 * @author weilong.hu
 * @since 2021/12/02 17:03
 */
public interface BuildProvider extends Provider {
    <T> T build();
}

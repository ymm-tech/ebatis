package io.manbang.ebatis.core.provider;

/**
 * missing是一个对象属性字段，所以无法通过注解来指定，或者用 @Missing 注解吧
 *
 * @author 章多亮
 * @since 2020/1/3 12:52
 */
@FunctionalInterface
public interface MissingProvider extends Provider {
    /**
     * 获取确实默认值
     *
     * @return 默认值
     */
    Object getMissing();
}

package io.manbang.ebatis.core.provider;

/**
 * @author weilong.hu
 * @since 2021/6/7 17:14
 */
public interface SearchAfterProvider extends Provider {
    /**
     * search after的排序值必须等于查询中的排序字段的数量，并且它们应该是相同的类型
     *
     * @return 排序值
     */
    Object[] sortValues();
}

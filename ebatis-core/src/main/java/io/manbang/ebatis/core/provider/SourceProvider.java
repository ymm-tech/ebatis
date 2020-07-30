package io.manbang.ebatis.core.provider;

/**
 * 排除字段和包含字段提供者，用于指定ES查询时候返回的字段列表信息
 *
 * @author 章多亮
 */
public interface SourceProvider extends Provider {
    /**
     * 获取包含的字段列表
     *
     * @return 字段列表
     */
    String[] getIncludeFields();

    /**
     * 获取排除的字段列表
     *
     * @return 字段列表
     */
    default String[] getExcludeFields() {
        return null; // NOSONAR
    }
}

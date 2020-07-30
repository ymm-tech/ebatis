package io.manbang.ebatis.core.domain;

/**
 * 可排序条件
 *
 * @author duoliang.zhang
 */
public interface Sortable {
    /**
     * 获取排序列表
     *
     * @return 排序列表
     */
    Sort[] getSorts();
}

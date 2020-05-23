package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.Sort;

/**
 * 排序提供者
 *
 * @author 章多亮
 * @since 2019/12/24 17:55
 */
@FunctionalInterface
public interface SortProvider extends Provider {
    /**
     * 获取排序列表
     *
     * @return 排序列表
     */
    Sort[] getSorts();
}

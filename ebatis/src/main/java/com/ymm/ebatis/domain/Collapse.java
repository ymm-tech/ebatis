package com.ymm.ebatis.domain;

import org.elasticsearch.search.collapse.CollapseBuilder;

/**
 * 折叠对象
 *
 * @author weilong.hu
 */
public interface Collapse {
    /**
     * 折叠字段
     *
     * @param field field name
     * @return Collapse
     */
    static Collapse collapse(String field) {
        return new DefaultCollapse(field);
    }

    /**
     * 转换成折叠对象
     *
     * @return 折叠对象
     */
    CollapseBuilder toCollapseBuilder();
}

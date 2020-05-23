package com.ymm.ebatis.core.domain;

import org.elasticsearch.search.collapse.CollapseBuilder;

/**
 * @author weilong.hu
 */
class DefaultCollapse implements Collapse {
    private String field;

    public DefaultCollapse(String field) {
        this.field = field;
    }

    @Override
    public CollapseBuilder toCollapseBuilder() {
        return new CollapseBuilder(field);
    }
}

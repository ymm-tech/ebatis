package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.domain.Collapse;

/**
 * collapse 折叠字段
 *
 * @author weilong.hu
 */
public interface CollapseProvider extends Provider {
    /**
     * 获取折叠聚合
     *
     * @return Collapse
     */
    Collapse getCollapse();
}

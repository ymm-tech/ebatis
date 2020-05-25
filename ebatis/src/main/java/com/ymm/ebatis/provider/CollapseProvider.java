package com.ymm.ebatis.provider;

import com.ymm.ebatis.domain.Collapse;

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

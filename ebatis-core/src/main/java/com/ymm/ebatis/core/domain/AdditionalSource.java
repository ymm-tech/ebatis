package com.ymm.ebatis.core.domain;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 */
public interface AdditionalSource {
    /**
     * 设置搜索的 additional fields
     *
     * @param additionalSource 附加
     */
    void setAdditionalSource(Map<String, List<Object>> additionalSource);
}

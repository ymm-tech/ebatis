package io.manbang.ebatis.core.domain;

import java.util.List;
import java.util.Map;

/**
 * @author weilong.hu
 * @since 2021/5/14 10:52
 */
public interface HighlightSource {
    void setHighlightSource(Map<String, List<String>> highlightSource);
}

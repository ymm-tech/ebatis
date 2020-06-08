package com.ymm.ebatis.core.provider;

import com.ymm.ebatis.core.annotation.Ignore;

/**
 * @author 章多亮
 * @since 2020/6/8 15:25
 */
public class DefaultScrollProvider implements ScrollProvider {
    @Ignore
    private boolean clear;
    @Ignore
    private String scrollId;

    public void clearScroll() {
        this.clear = true;
    }

    @Override
    public boolean isClearScroll() {
        return clear;
    }

    @Override
    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}

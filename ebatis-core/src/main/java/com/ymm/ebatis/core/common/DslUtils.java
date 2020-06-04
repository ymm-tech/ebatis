package com.ymm.ebatis.core.common;

import org.elasticsearch.common.unit.TimeValue;

/**
 * @author 章多亮
 * @since 2019/12/18 16:33
 */
public class DslUtils {
    private DslUtils() {
        throw new UnsupportedOperationException();
    }

    public static TimeValue getScrollKeepAlive(long keepAlive) {
        return keepAlive <= 0 ? null : TimeValue.timeValueMillis(keepAlive);
    }
}


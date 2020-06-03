package com.ymm.ebatis.core.common;

import org.elasticsearch.common.unit.TimeValue;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 章多亮
 * @since 2019/12/18 16:33
 */
public class DslUtils {
    private DslUtils() {
        throw new UnsupportedOperationException();
    }

    public static Object dateToMillis(Object value) {
        if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Calendar) {
            return ((Calendar) value).getTimeInMillis();
        } else {
            return value;
        }
    }

    public static TimeValue getScrollKeepAlive(long keepAlive) {
        return keepAlive <= 0 ? null : TimeValue.timeValueMillis(keepAlive);
    }
}

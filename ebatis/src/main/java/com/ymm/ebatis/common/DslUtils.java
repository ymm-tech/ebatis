package com.ymm.ebatis.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

    public static String getRouting(String... routing) {
        if (ArrayUtils.isEmpty(routing)) {
            return null;
        }
        if (routing.length == 1) {
            if (StringUtils.isBlank(routing[0])) {
                return null;
            } else {
                return routing[0];
            }
        } else {
            return StringUtils.join(routing, ",");
        }
    }
}

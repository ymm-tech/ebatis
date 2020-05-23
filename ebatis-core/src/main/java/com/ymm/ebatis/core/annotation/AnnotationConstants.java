package com.ymm.ebatis.core.annotation;

/**
 * @author 章多亮
 * @since 2019/12/18 15:49
 */
public class AnnotationConstants {
    public static final long TIMEOUT_NO_SET = -1L;
    public static final String NO_SET = "";
    public static final String[] ROUTING_NO_SET = {};
    public static final int ACTIVE_SHARD_COUNT_DEFAULT = -2;

    private AnnotationConstants() {
        throw new UnsupportedOperationException();
    }
}

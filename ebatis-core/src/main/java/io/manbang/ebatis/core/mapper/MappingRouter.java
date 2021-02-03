package io.manbang.ebatis.core.mapper;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author weilong.hu
 * @since 2021/2/3 14:39
 */
public interface MappingRouter {
    String[] indices();

    default String[] types() {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }
}

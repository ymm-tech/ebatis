package com.ymm.ebatis.meta;

import java.lang.reflect.Field;

/**
 * @author 章多亮
 * @since 2020/5/28 10:39
 */
public interface FieldClassMeta extends ClassMeta {
    Field getField();
}

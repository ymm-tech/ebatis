package com.ymm.ebatis.meta;

import com.ymm.ebatis.exception.EbatisException;

import java.lang.reflect.Field;

/**
 * @author 章多亮
 * @since 2020/5/28 10:39
 */
class CachedFieldClassMeta extends AbstractClassMeta implements FieldClassMeta {
    private final Field field;

    private CachedFieldClassMeta(Field field, Class<?> fieldType) {
        super(fieldType);
        this.field = field;
    }

    static ClassMeta createIfAbsent(Field field, Class<?> fieldType) {
        return CLASS_METAS.computeIfAbsent(fieldType, t -> create(field, t));
    }

    private static ClassMeta create(Field field, Class<?> fieldType) {
        if (!field.getType().isAssignableFrom(fieldType)) {
            throw new EbatisException("字段类型和实际类型不兼容");
        }
        return new CachedFieldClassMeta(field, fieldType);
    }

    @Override
    public Field getField() {
        return field;
    }
}

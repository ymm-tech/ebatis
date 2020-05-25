package com.ymm.ebatis.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 属性元数据
 *
 * @author 章多亮
 * @since 2020/5/21 14:53
 */
public interface FieldMeta extends AnnotatedMeta<Field> {

    /**
     * 创建指定字段元数据
     *
     * @param field 字段
     * @return 字段元数据
     */
    static FieldMeta from(Field field) {
        return new DefaultFieldMeta(field);
    }

    /**
     * 注解元素就是字段本身 {@link FieldMeta#getField()}
     *
     * @return 字段
     */
    @Override
    default Field getElement() {
        return getField();
    }

    /**
     * 获取字段
     *
     * @return 字段
     */
    Field getField();

    /**
     * 获取读取字段的方法
     *
     * @return 读取字段的方法
     */
    Method getReadMethod();

    /**
     * 获取字段类型
     *
     * @return 字段类型
     */
    @Override
    default Class<?> getType() {
        return getField().getType();
    }

    /**
     * 获取字段的逻辑名称
     *
     * @return 字段名
     */
    String getName();
}

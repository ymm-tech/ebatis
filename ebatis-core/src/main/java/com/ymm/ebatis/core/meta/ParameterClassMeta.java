package com.ymm.ebatis.core.meta;

/**
 * 类元数据
 *
 * @author 章多亮
 * @since 2020/5/21 19:11
 */
public interface ParameterClassMeta extends AnnotatedMeta<Class<?>> {
    /**
     * 从指定 Class 创建类元信息
     *
     * @param clazz 类
     * @return 元信息
     */
    static ParameterClassMeta from(Class<?> clazz) {
        return new DefaultParameterClassMeta(clazz);
    }

    /**
     * 获取被注解的类型
     *
     * @return 类
     */
    @Override
    default Class<?> getElement() {
        return getType();
    }

    /**
     * 获取类的字段列表元数据
     *
     * @return 字段元数据列表
     */
    FieldMeta[] getFields();
}


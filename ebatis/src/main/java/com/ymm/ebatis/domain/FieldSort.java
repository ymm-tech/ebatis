package com.ymm.ebatis.domain;

/**
 * @author 章多亮
 * @since 2020/1/6 19:34
 */
public interface FieldSort extends Sort {
    /**
     * 获取缺失字段默认值
     *
     * @return 缺失字段默认值
     */
    Object missing();

    /**
     * 设置缺失字段默认值
     *
     * @param missing 缺失字段默认值
     * @return 字段排序对象
     */
    FieldSort missing(Object missing);

    /**
     * 获取未映射类型
     *
     * @return 未映射类型
     */
    String unmappedType();

    /**
     * 设置未映射类型
     *
     * @param unmappedType 未映射类型
     * @return 字段排序对象
     */
    FieldSort unmappedType(String unmappedType);
}

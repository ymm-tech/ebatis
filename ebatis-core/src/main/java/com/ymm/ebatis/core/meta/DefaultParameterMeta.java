package com.ymm.ebatis.core.meta;

import lombok.ToString;

import java.lang.reflect.Parameter;

/**
 * 参数元数据默认实现
 *
 * @author 章多亮
 * @since 2020/5/21 14:48
 */
@ToString
class DefaultParameterMeta implements ParameterMeta {
    private final int index;
    private final Parameter parameter;

    DefaultParameterMeta(int index, Parameter parameter) {
        this.index = index;
        this.parameter = parameter;
    }

    @Override
    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public int getIndex() {
        return index;
    }

}

package com.ymm.ebatis.core.meta;

import java.lang.reflect.Parameter;

/**
 * @author 章多亮
 * @since 2020/5/28 9:44
 */
public interface ParameterClassMeta extends ClassMeta {
    Parameter getParameter();
}

package io.manbang.ebatis.core.exception;

import io.manbang.ebatis.core.annotation.Mapper;

/**
 * Mapper接口必须加上 {@link Mapper} 注解
 *
 * @author 章多亮
 * @since 2020/5/29 15:00
 */
public class MapperAnnotationNotPresentException extends EbatisException {
    private static final long serialVersionUID = -2677240466187503166L;

    public MapperAnnotationNotPresentException(String message) {
        super(message);
    }
}

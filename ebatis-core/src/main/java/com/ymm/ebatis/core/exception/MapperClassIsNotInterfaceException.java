package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2020/5/22 15:23
 */
public class MapperClassIsNotInterfaceException extends EbatisException {
    private static final long serialVersionUID = 4948715688294473569L;

    public MapperClassIsNotInterfaceException(String message) {
        super(message);
    }
}

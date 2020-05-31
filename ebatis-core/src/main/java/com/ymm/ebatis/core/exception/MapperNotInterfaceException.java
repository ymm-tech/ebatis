package com.ymm.ebatis.core.exception;

/**
 * Mapper必须定义成接口
 *
 * @author 章多亮
 * @since 2020/5/22 15:23
 */
public class MapperNotInterfaceException extends EbatisException {
    private static final long serialVersionUID = 4948715688294473569L;

    public MapperNotInterfaceException(String message) {
        super(message);
    }
}

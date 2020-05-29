package com.ymm.ebatis.exception;

/**
 * Mapper接口不允许集成，单接口
 *
 * @author 章多亮
 * @since 2020/5/29 15:04
 */
public class MapperNotAllowInheritException extends EbatisException {
    private static final long serialVersionUID = 467113492064969510L;

    public MapperNotAllowInheritException(String message) {
        super(message);
    }
}

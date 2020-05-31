package com.ymm.ebatis.core.exception;

/**
 * 非法的响应
 *
 * @author 章多亮
 * @since 2020/01/12 15:14:06
 */
public class InvalidResponseException extends EbatisException {
    private static final long serialVersionUID = 1702970144271771920L;

    public InvalidResponseException(String msg) {
        super(msg);
    }
}

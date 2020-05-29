package com.ymm.ebatis.exception;

/**
 * 请求类型不支持
 *
 * @author 章多亮
 * @since 2020/5/21 14:17
 */
public class RequestTypeNotSupportException extends EbatisException {
    private static final long serialVersionUID = 199740846251007800L;

    public RequestTypeNotSupportException(String message) {
        super(message);
    }
}

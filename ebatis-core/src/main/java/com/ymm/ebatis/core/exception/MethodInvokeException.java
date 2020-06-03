package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2020/6/3 14:33
 */
public class MethodInvokeException extends EbatisException {
    private static final long serialVersionUID = 7314789847168353784L;

    public MethodInvokeException(Exception cause) {
        super(cause);
    }
}

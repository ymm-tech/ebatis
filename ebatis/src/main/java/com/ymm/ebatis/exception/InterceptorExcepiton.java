package com.ymm.ebatis.exception;

/**
 * @author weilong.hu
 * @date 2020-04-21
 */
public class InterceptorExcepiton extends EbatisException {
    private static final long serialVersionUID = -2355467883498121787L;

    public InterceptorExcepiton(String message, Throwable cause) {
        super(message, cause);
    }
}

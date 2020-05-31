package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2019/12/17 18:35
 */
public class EbatisException extends RuntimeException {
    private static final long serialVersionUID = -6750356543275127598L;

    public EbatisException() {
        this("");
    }

    public EbatisException(String message) {
        this(message, null);
    }

    public EbatisException(String message, Throwable cause) {
        this(message, cause, false, false);
    }

    public EbatisException(Throwable cause) {
        this(null, cause);
    }

    protected EbatisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

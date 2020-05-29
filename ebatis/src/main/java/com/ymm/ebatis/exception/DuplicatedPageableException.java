package com.ymm.ebatis.exception;

/**
 * @author 章多亮
 * @since 2020/5/25 13:42
 */
public class DuplicatedPageableException extends EbatisException {
    private static final long serialVersionUID = -5325992031689570617L;

    public DuplicatedPageableException(String message) {
        super(message);
    }
}

package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2020/6/1 15:48
 */
public class DocumentDeserializeException extends EbatisException {
    private static final long serialVersionUID = 1033847024904831457L;

    public DocumentDeserializeException(Exception cause) {
        super(cause);
    }
}

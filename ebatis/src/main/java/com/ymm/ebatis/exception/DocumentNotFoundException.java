package com.ymm.ebatis.exception;

/**
 * @author 章多亮
 * @since 2020/5/29 11:05
 */
public class DocumentNotFoundException extends EbatisException {
    private static final long serialVersionUID = -8778944431522730017L;

    public DocumentNotFoundException(String message) {
        super(message);
    }
}

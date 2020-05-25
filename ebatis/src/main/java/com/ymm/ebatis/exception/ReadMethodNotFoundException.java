package com.ymm.ebatis.exception;

public class ReadMethodNotFoundException extends QueryDslException {
    private static final long serialVersionUID = -3875701285776717719L;

    public ReadMethodNotFoundException(String msg) {
        super(msg);
    }
}

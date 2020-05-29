package com.ymm.ebatis.exception;

/**
 * @author 章多亮
 * @since 2020/5/29 9:30
 */
public class ConditionNotFoundException extends EbatisException {
    private static final long serialVersionUID = 6807362815546830872L;

    public ConditionNotFoundException(String message) {
        super(message);
    }
}

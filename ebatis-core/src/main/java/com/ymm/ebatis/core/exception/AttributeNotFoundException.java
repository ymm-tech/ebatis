package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2020/6/3 13:23
 */
public class AttributeNotFoundException extends EbatisException {
    private static final long serialVersionUID = 538894359279256953L;

    public AttributeNotFoundException(String message) {
        super(message);
    }
}

package com.ymm.ebatis.core.exception;

/**
 * 请求工厂未找到
 *
 * @author 章多亮
 * @since 2020/5/21 14:17
 */
public class RequestFactoryNotFoundException extends EbatisException {
    private static final long serialVersionUID = 199740846251007800L;

    public RequestFactoryNotFoundException(String message) {
        super(message);
    }
}

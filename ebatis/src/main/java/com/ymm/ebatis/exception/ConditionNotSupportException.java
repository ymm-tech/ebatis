package com.ymm.ebatis.exception;

/**
 * @author duoliang.zhang
 */
public class ConditionNotSupportException extends QueryDslException {
    private static final long serialVersionUID = -4218025485622240707L;

    public ConditionNotSupportException() {
    }

    public ConditionNotSupportException(String msg) {
        super(msg);
    }
}

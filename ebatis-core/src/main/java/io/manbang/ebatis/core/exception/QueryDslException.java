package io.manbang.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2019/12/18 16:36
 */
public class QueryDslException extends RuntimeException {
    private static final long serialVersionUID = 7069438085014307653L;

    public QueryDslException() {
        super();
    }

    public QueryDslException(String message) {
        super(message);
    }

    public QueryDslException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryDslException(Throwable cause) {
        super(cause);
    }

    protected QueryDslException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

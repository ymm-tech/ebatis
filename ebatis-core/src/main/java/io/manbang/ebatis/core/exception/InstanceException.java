package io.manbang.ebatis.core.exception;

/**
 * @author weilong.hu
 * @since 2021/2/3 15:39
 */
public class InstanceException extends EbatisException {

    private static final long serialVersionUID = 6754131368747355793L;

    public InstanceException(Exception cause) {
        super(cause);
    }
}

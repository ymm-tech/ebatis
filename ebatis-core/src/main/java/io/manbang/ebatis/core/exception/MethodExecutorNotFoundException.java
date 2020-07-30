package io.manbang.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2019/12/20 15:46
 */
public class MethodExecutorNotFoundException extends EbatisException {
    private static final long serialVersionUID = 5658881348230278389L;

    public MethodExecutorNotFoundException(String message) {
        super(message);
    }
}

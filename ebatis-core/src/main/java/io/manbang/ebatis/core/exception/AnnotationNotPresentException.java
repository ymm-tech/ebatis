package io.manbang.ebatis.core.exception;

/**
 * 需要的注解没有
 *
 * @author 章多亮
 * @since 2020/1/2 19:29
 */
public class AnnotationNotPresentException extends EbatisException {
    private static final long serialVersionUID = 6443666403791653809L;

    public AnnotationNotPresentException() {
        super();
    }

    public AnnotationNotPresentException(String msg) {
        super(msg);
    }
}

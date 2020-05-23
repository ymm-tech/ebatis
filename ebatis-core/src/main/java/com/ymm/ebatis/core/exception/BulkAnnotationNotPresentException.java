package com.ymm.ebatis.core.exception;

/**
 * @author 章多亮
 * @since 2019/12/26 19:51
 */
public class BulkAnnotationNotPresentException extends EbatisException {
    private static final long serialVersionUID = -5669444715101989019L;

    public BulkAnnotationNotPresentException(String msg) {
        super(msg);
    }
}

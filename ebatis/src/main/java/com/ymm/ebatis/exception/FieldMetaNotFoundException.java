package com.ymm.ebatis.exception;

/**
 * 字段元数据信息未找到
 *
 * @author 章多亮
 * @since 2020/5/26 10:49
 */
public class FieldMetaNotFoundException extends EbatisException {
    private static final long serialVersionUID = -5493308748390750338L;

    public FieldMetaNotFoundException(String message) {
        super(message);
    }
}

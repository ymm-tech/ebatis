package io.manbang.ebatis.core.exception;

/**
 * 方法元数据信息找不到
 *
 * @author 章多亮
 * @since 2020/5/26 10:46
 */
public class MethodMetaNotFoundException extends EbatisException {
    private static final long serialVersionUID = -6568314608865771107L;

    public MethodMetaNotFoundException(String message) {
        super(message);
    }
}

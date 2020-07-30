package io.manbang.ebatis.core.exception;

/**
 * 接口方法重复定义响应提取器
 *
 * @author 章多亮
 * @since 2020/5/25 13:43
 */
public class DuplicatedResponseExtractorException extends EbatisException {
    private static final long serialVersionUID = -7666464820430210166L;

    public DuplicatedResponseExtractorException(String message) {
        super(message);
    }
}

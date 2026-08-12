package io.manbang.ebatis.core.annotation;

/**
 * 匹配类型
 */
public enum MatchType {
    /**
     * 包含
     */
    CONTAINS,
    /**
     * 开头
     */
    STARTS_WITH,
    /**
     * 结尾
     */
    ENDS_WITH,
    /**
     * 精确匹配
     */
    EXACT;

    public String wrap(Object value) {
        switch (this) {
            case CONTAINS:
                return "*" + value + "*";
            case STARTS_WITH:
                return value + "*";
            case ENDS_WITH:
                return "*" + value;
            case EXACT:
                return value.toString();
            default:
                throw new IllegalArgumentException("Unknown match type: " + this);
        }
    }
}

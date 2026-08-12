package io.manbang.ebatis.core.annotation;

/**
 * 文档版本类型
 */
public enum VersionType {
    /**
     * 内部版本号，有 ES 自己控制版本
     */
    INTERNAL,
    /**
     * 外部版本号，由用户控制版本，索引或者更新的文档版本号，必须大于当前索引的版本
     */
    EXTERNAL,
    /**
     * 外部版本号，由用户控制版本，索引或者更新的文档版本号，必须大于等于当前索引的版本
     */
    EXTERNAL_GTE
}

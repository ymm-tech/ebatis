package com.ymm.ebatis.meta;

/**
 * @author 章多亮
 * @since 2020/5/29 12:02
 */
class CachedClassMeta extends AbstractClassMeta {
    private CachedClassMeta(Class<?> clazz) {
        super(clazz);
    }

    static <T> ClassMeta createIfAbsent(Class<T> clazz) {
        return CLASS_METAS.computeIfAbsent(clazz, CachedClassMeta::new);
    }
}

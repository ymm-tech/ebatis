package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Cat;
import io.manbang.ebatis.core.meta.MethodMeta;

class CatRequestFactory extends AbstractCatRequestFactory<CatRequest> {
    static final CatRequestFactory INSTANCE = new CatRequestFactory();

    private CatRequestFactory() {
    }

    @Override
    protected CatRequest doCreate(MethodMeta meta, Object[] args) {
        Cat cat = meta.getRequestAnnotation();
        return cat.catType().getRequestFactory().create(meta, args);
    }
}

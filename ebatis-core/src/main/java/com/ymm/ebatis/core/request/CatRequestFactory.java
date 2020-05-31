package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Cat;
import com.ymm.ebatis.core.meta.MethodMeta;

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

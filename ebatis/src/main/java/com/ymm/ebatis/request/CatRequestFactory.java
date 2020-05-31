package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Cat;
import com.ymm.ebatis.meta.MethodMeta;

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

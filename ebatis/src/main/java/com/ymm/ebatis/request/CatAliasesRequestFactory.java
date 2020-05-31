package com.ymm.ebatis.request;

import com.ymm.ebatis.meta.MethodMeta;

class CatAliasesRequestFactory extends AbstractCatRequestFactory<CatAliasesRequest> {
    static final CatAliasesRequestFactory INSTANCE = new CatAliasesRequestFactory();

    private CatAliasesRequestFactory() {
    }

    @Override
    protected CatAliasesRequest doCreate(MethodMeta meta, Object[] args) {
        return new CatAliasesRequest();
    }
}

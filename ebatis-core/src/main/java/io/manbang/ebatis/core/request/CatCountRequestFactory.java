package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Cat;
import io.manbang.ebatis.core.meta.MetaUtils;
import io.manbang.ebatis.core.meta.MethodMeta;

class CatCountRequestFactory extends AbstractCatRequestFactory<CatCountRequest> {
    static final CatCountRequestFactory INSTANCE = new CatCountRequestFactory();

    private CatCountRequestFactory() {
    }

    @Override
    protected void setAnnotationMeta(CatCountRequest request, Cat cat) {
        MetaUtils.findFirstElement(cat.count())
                .ifPresent(count -> request.index(count.index())
                        .format(count.format()));
    }

    @Override
    protected CatCountRequest doCreate(MethodMeta meta, Object[] args) {
        return new CatCountRequest();
    }
}

package com.ymm.ebatis.core.request;

import com.ymm.ebatis.core.annotation.Cat;

abstract class AbstractCatRequestFactory<R extends CatRequest> extends AbstractRequestFactory<Cat, R> {
    @Override
    protected void setAnnotationMeta(R request, Cat cat) {
        // do nothing
    }
}

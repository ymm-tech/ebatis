package com.ymm.ebatis.request;

import com.ymm.ebatis.annotation.Cat;

abstract class AbstractCatRequestFactory<R extends CatRequest> extends AbstractRequestFactory<Cat, R> {
    @Override
    protected void setAnnotationMeta(R request, Cat cat) {
        // do nothing
    }
}

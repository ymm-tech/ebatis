package io.manbang.ebatis.core.request;

import io.manbang.ebatis.core.annotation.Cat;

abstract class AbstractCatRequestFactory<R extends CatRequest> extends AbstractRequestFactory<Cat, R> {
    @Override
    protected void setAnnotationMeta(R request, Cat cat) {
        // do nothing
    }
}

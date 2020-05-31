package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

@AutoService(ResponseExtractorProvider.class)
public class WrappedGetResponseExtractorProvider extends AbstractGetResponseExtractorProvider {
    public WrappedGetResponseExtractorProvider() {
        super(ResultType.OPTIONAL, ResultType.COMPLETABLE_FUTURE);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }
}

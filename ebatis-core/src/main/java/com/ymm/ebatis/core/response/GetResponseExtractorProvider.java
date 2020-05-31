package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

@AutoService(ResponseExtractorProvider.class)
public class GetResponseExtractorProvider extends AbstractGetResponseExtractorProvider {
    public GetResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 14:49
 */
@AutoService(ResponseExtractorProvider.class)
public class SearchResponseExtractorProvider extends AbstractSearchResponseExtractorProvider {
    public SearchResponseExtractorProvider() {
        super(ResultType.OTHER, ResultType.LIST, ResultType.PAGE);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

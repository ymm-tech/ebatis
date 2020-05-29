package com.ymm.ebatis.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 14:28
 */
@AutoService(ResponseExtractorProvider.class)
public class WrappedSearchResponseExtractorProvider extends AbstractSearchResponseExtractorProvider {
    public WrappedSearchResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }


    @Override
    protected boolean isWrapped() {
        return true;
    }
}

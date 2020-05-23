package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 14:28
 */
public class WrappedSearchResponseExtractorProvider extends AbstractSearchResponseExtractorProvider {
    public WrappedSearchResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }


    @Override
    protected boolean isWrapped() {
        return true;
    }
}

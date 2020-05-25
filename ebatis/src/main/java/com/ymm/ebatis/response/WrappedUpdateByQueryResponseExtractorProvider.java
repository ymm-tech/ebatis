package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:11
 */
public class WrappedUpdateByQueryResponseExtractorProvider extends AbstractUpdateByQueryResponseExtractorProvider {
    public WrappedUpdateByQueryResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }

}

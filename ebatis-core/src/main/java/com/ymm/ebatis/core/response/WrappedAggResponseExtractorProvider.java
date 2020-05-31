package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 16:39
 */
@AutoService(ResponseExtractorProvider.class)
public class WrappedAggResponseExtractorProvider extends AbstractAggResponseExtractorProvider {
    public WrappedAggResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }
}

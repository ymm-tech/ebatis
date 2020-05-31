package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:26
 */
@AutoService(ResponseExtractorProvider.class)
public class WrappedMultiSearchResponseExtractorProvider extends AbstractMultiSearchResponseExtractorProvider {
    public WrappedMultiSearchResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }
}

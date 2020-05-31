package com.ymm.ebatis.core.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:11
 */
@AutoService(ResponseExtractorProvider.class)
public class WrappedUpdateByQueryResponseExtractorProvider extends AbstractUpdateByQueryResponseExtractorProvider {
    public WrappedUpdateByQueryResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }

}

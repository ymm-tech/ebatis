package com.ymm.ebatis.response;

import com.google.auto.service.AutoService;
import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 14:07
 */
@AutoService(ResponseExtractorProvider.class)
public class WrappedDeleteByQueryResponseExtractorProvider extends AbstractDeleteByQueryResponseExtractorProvider {
    public WrappedDeleteByQueryResponseExtractorProvider() {
        super(ResultType.COMPLETABLE_FUTURE, ResultType.OPTIONAL);
    }

    @Override
    protected boolean isWrapped() {
        return true;
    }
}

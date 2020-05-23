package com.ymm.ebatis.core.response;

import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:31
 */
public class OtherUpdateByQueryResponseExtractorProvider extends AbstractUpdateByQueryResponseExtractorProvider {
    public OtherUpdateByQueryResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

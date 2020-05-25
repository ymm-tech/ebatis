package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 14:49
 */
public class OtherSearchResponseExtractorProvider extends AbstractSearchResponseExtractorProvider {
    public OtherSearchResponseExtractorProvider() {
        super(ResultType.OTHER, ResultType.LIST, ResultType.PAGE);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

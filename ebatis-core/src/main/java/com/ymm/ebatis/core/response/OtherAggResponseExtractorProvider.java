package com.ymm.ebatis.core.response;


import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 16:52
 */
public class OtherAggResponseExtractorProvider extends AbstractAggResponseExtractorProvider {
    public OtherAggResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

package com.ymm.ebatis.response;


import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 14:02
 */
public class OtherDeleteResponseExtractorProvider extends AbstractDeleteResponseExtractorProvider {
    public OtherDeleteResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

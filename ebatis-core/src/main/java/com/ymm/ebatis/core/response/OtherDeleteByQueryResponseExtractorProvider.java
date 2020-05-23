package com.ymm.ebatis.core.response;


import com.ymm.ebatis.core.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 14:10
 */
public class OtherDeleteByQueryResponseExtractorProvider extends AbstractDeleteByQueryResponseExtractorProvider {
    public OtherDeleteByQueryResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

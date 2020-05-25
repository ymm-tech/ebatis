package com.ymm.ebatis.response;


import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 17:05
 */
public class OtherIndexResponseExtractorProvider extends AbstractIndexResponseExtractorProvider {
    public OtherIndexResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}

package com.ymm.ebatis.response;

import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/18 11:37
 */
public class OtherUpdateResponseExtractorProvider extends AbstractUpdateResponseExtractorProvider {
    public OtherUpdateResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }

}

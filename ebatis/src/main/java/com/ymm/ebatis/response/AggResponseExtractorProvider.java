package com.ymm.ebatis.response;


import com.google.auto.service.AutoService;
import com.ymm.ebatis.meta.ResultType;

/**
 * @author 章多亮
 * @since 2020/1/17 16:52
 */
@AutoService(ResponseExtractorProvider.class)
public class AggResponseExtractorProvider extends AbstractAggResponseExtractorProvider {
    public AggResponseExtractorProvider() {
        super(ResultType.OTHER);
    }

    @Override
    protected boolean isWrapped() {
        return false;
    }
}
